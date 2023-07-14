package ru.practicum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.ewm.client.stats.StatsClient;

@Configuration
public class ConfigClient {

    private final String serverUrl;

    public ConfigClient(@Value("http://stat-server:9090") String serverUrl) {
        this.serverUrl = serverUrl;
    }

    @Bean
    public StatsClient statClient() {
        return new StatsClient(serverUrl);
    }

}
