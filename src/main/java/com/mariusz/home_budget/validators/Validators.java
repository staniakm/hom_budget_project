package com.mariusz.home_budget.validators;

import com.mariusz.home_budget.entity.form.MoneyFlowForm;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class Validators {

    public static Optional<String> validateBigDecimal(String amount) {

        return Optional.empty();
    }

    @Bean
    public EmailValidator emailValidator(){
        return EmailValidator.getInstance();
    }

    public static Optional<String> validateMoneyHolder(String holder){
        if (holder==null || holder.trim().length()==0){
            return Optional.of("Incorrect money holder selected.");
        }else {
            try {
                Long.parseLong(holder);
            }catch (Exception ex){
                return Optional.of("Incorrect money holder.");
            }
        }
        return Optional.empty();
    }



}
