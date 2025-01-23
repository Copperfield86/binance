package com.example.binance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // Włączenie obsługi harmonogramów (Scheduled Tasks)
public class BinanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BinanceApplication.class, args);
    }
}
