package com.jb.config;

import com.jb.client.ClientSession;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
public class AppConfiguration {

    @Bean(name = "tokens")
    public Map<String, ClientSession> tokenMap() {
        return new HashMap<>();
    }

    @Bean
    public UUID uuid() {
        return UUID.randomUUID();
    }


}
