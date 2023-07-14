package ru.main_service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.stat_client.StatClient;

@Configuration
public class ConfigClient {

    private final String serverUrl;

    public ConfigClient(@Value("http://stat-server:9090") String serverUrl) {
        this.serverUrl = serverUrl;
    }

    @Bean
    public StatClient statClient() {
        return new StatClient(serverUrl);
    }

}
