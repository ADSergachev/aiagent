package ru.sergchev.aiagent.configurations;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfiguration {

    private final ChatClient chatClient;

    public AiConfiguration(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @Bean
    public ChatClient aiClient() {
        return chatClient;
    }
}
