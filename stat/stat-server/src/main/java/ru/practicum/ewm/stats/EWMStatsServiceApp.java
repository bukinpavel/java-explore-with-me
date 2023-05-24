package ru.practicum.ewm.stats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.Entity;

@SpringBootApplication
@EnableTransactionManagement
@EnableJpaRepositories
@EntityScan("ru.practicum.ewm.dto.stats")
public class EWMStatsServiceApp {
    public static void main(String[] args){
        SpringApplication.run(EWMStatsServiceApp.class, args);
    }
}
