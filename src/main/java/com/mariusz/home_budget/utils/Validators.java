package com.mariusz.home_budget.utils;

import com.mariusz.home_budget.entity.form.MoneyFlowForm;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class Validators {

    public static Optional<String> validMoneyFlowOperation(MoneyFlowForm newOperation) {

        return Optional.empty();
    }

    @Bean
    public EmailValidator emailValidator(){
        return EmailValidator.getInstance();
    }



}
