package com.weatherapp.weatherapp.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class conguration {
    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder.build();
    }

}
