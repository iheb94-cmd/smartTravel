package com.example.SmartTravel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class SmartTravelApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmartTravelApplication.class, args);
    }
}