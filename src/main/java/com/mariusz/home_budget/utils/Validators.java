package com.mariusz.home_budget.utils;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Validators {

    @Bean
    public EmailValidator emailValidator(){
        return EmailValidator.getInstance();
    }
}
