package ru.sergchev.aiagent.tools;

import org.springframework.ai.tool.annotation.Tool;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.ComputerSystem;
import oshi.hardware.GlobalMemory;
import oshi.hardware.GraphicsCard;
import oshi.hardware.HardwareAbstractionLayer;

import java.util.List;

public class ClientProcessor {

    @Tool(description = "Возвращает полные характеристики компьютера пользователя: модель процессора, видеокарты, объем оперативной памяти, производитель компьютера, материнскую плату и базовую аппаратную информацию")
    public String getPcCharacteristics() {
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();

        CentralProcessor cpu = hal.getProcessor();
        GlobalMemory memory = hal.getMemory();
        ComputerSystem cs = hal.getComputerSystem();
        List<GraphicsCard> gpus = hal.getGraphicsCards();

        StringBuilder sb = new StringBuilder();

        sb.append("Характеристики компьютера:\n\n");

        sb.append("Процессор:\n");
        sb.append("- Название: ").append(safe(cpu.getProcessorIdentifier().getName())).append("\n");
        sb.append("- Архитектура: ").append(System.getProperty("os.arch")).append("\n");
        sb.append("- Физические ядра: ").append(cpu.getPhysicalProcessorCount()).append("\n");
        sb.append("- Логические ядра: ").append(cpu.getLogicalProcessorCount()).append("\n\n");

        sb.append("Оперативная память:\n");
        sb.append("- Общий объем ОЗУ: ").append(formatBytes(memory.getTotal())).append("\n");
        sb.append("- Доступно сейчас: ").append(formatBytes(memory.getAvailable())).append("\n\n");

        sb.append("Видеокарта:\n");
        if (gpus == null || gpus.isEmpty()) {
            sb.append("- Не удалось получить информацию о видеокарте\n\n");
        } else {
            for (int i = 0; i < gpus.size(); i++) {
                GraphicsCard gpu = gpus.get(i);
                sb.append("- GPU ").append(i + 1).append(":\n");
                sb.append("  - Название: ").append(safe(gpu.getName())).append("\n");
                sb.append("  - Производитель: ").append(safe(gpu.getVendor())).append("\n");
                sb.append("  - Device ID: ").append(safe(gpu.getDeviceId())).append("\n");
                sb.append("  - VRAM: ").append(formatBytes(gpu.getVRam())).append("\n");
                sb.append("  - Версия/ревизия: ").append(safe(gpu.getVersionInfo())).append("\n");
            }
            sb.append("\n");
        }

        sb.append("Компьютер:\n");
        sb.append("- Производитель: ").append(safe(cs.getManufacturer())).append("\n");
        sb.append("- Модель: ").append(safe(cs.getModel())).append("\n");
        sb.append("- Серийный номер: ").append(safe(cs.getSerialNumber())).append("\n\n");

        if (cs.getBaseboard() != null) {
            sb.append("Материнская плата:\n");
            sb.append("- Производитель: ").append(safe(cs.getBaseboard().getManufacturer())).append("\n");
            sb.append("- Модель: ").append(safe(cs.getBaseboard().getModel())).append("\n");
            sb.append("- Версия: ").append(safe(cs.getBaseboard().getVersion())).append("\n");
            sb.append("- Серийный номер: ").append(safe(cs.getBaseboard().getSerialNumber())).append("\n\n");
        }

        if (cs.getFirmware() != null) {
            sb.append("BIOS/UEFI:\n");
            sb.append("- Производитель: ").append(safe(cs.getFirmware().getManufacturer())).append("\n");
            sb.append("- Название: ").append(safe(cs.getFirmware().getName())).append("\n");
            sb.append("- Описание: ").append(safe(cs.getFirmware().getDescription())).append("\n");
            sb.append("- Версия: ").append(safe(cs.getFirmware().getVersion())).append("\n");
        }

        System.out.println(sb);

        return sb.toString().trim();
    }

    private static String safe(String value) {
        return value == null || value.isBlank() ? "Неизвестно" : value;
    }

    private static String formatBytes(long bytes) {
        if (bytes <= 0) {
            return "Неизвестно";
        }

        double gb = bytes / 1024d / 1024d / 1024d;
        if (gb >= 1) {
            return String.format("%.2f GB", gb);
        }

        double mb = bytes / 1024d / 1024d;
        return String.format("%.2f MB", mb);
    }
}