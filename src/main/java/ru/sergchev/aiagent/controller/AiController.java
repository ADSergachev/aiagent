package ru.sergchev.aiagent.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
public class AiController {

    private final ChatClient chatClient;

    public AiController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @PostMapping(value = "/chat", consumes = MediaType.TEXT_PLAIN_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public Flux<String> chat(@RequestBody String userInput) {
        return chatClient
                .prompt("""
                        Твоя роль - самый лучший компьютрный мастер в мире. Твой опыт более 50 лет.
                        
                        Ты получаешь характеристики компьютера и должен пранализировать их.
                        Сказать плюсы и минусы.
                        Добавить сводную таблицу.
                        Отвечать всегда на русском.
                        Без личного мнения и не говори что ты самый лучший компьютрный мастер в мире.
                        """)
                .user(userInput)
                .stream()
                .content()
                .bufferTimeout(12, Duration.ofMillis(8000))
                .map(list -> String.join("", list));
    }


}
