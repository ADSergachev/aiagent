package ru.sergchev.aiagent.controller;

import lombok.AllArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sergchev.aiagent.tools.ClientProcessor;

@AllArgsConstructor
@RestController
public class AiController {

    private final ChatClient aiClient;

    @PostMapping(value = "/analyze-pc", consumes = MediaType.TEXT_PLAIN_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public String analyzePc() {

        return aiClient
                .prompt()
                .system("""
                        Ты помощник по анализу компьютеров.
                        Используй инструмент для получения характеристик компьютера.
                        Не придумывай характеристики от себя.
                        Не используй смайлики.
                        """)
                .user("""
                        Возьми из инструмента характеристики компьютера.
                        Оцени каждый компонент отдельно.
                        Верни:
                        - список компонентов,
                        - их характеристики,
                        - краткую оценку каждого компонента,
                        - общую оценку от 0 до 100,
                        - объяснение итоговой оценки.
                        """)
                .tools(new ClientProcessor())
                .call()
                .content();
    }


}
