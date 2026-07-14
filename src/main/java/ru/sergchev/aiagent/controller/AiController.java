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
    public String chat() {
        String response = aiClient
                .prompt("Возьми из тулов характеристики компьютера. Оцени покомпонентно. Сделай таблицу характеристик, в колонке сделай краткое оценточное описание. В конце дай оценку от 0 до 100 и дай пояснения, почему ты выбрал именно ее. Смайлики не используй")
                .tools(new ClientProcessor())
                .call()
                .content();

        return response;
    }


}
