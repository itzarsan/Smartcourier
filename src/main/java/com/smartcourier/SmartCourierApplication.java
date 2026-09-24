package com.smartcourier;

import com.smartcourier.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SmartCourierApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartCourierApplication.class, args);
    }
    
    @Bean
    CommandLineRunner init(UserService userService) {
        return args -> {
            userService.createAdminIfNotExists();
        };
    }
}
