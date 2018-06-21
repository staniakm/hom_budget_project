package com.mariusz.home_budget;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.context.request.RequestContextListener;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class HomeBudgetApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomeBudgetApplication.class, args);
    }

    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    //komentarz do sprawdzenia commitów
}
