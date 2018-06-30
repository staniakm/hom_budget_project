package com.mariusz.home_budget.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AmountValidator implements ConstraintValidator<Amount, String> {

    @Override
    public void initialize(Amount paramA) {
    }

    @Override
    public boolean isValid(String amount, ConstraintValidatorContext ctx) {
        if(amount == null){
            return false;
        }
        //validate amount value
        if (amount.matches("\\d+")) return true;
            //validating amount with with coma separator
        else if(amount.matches("^\\d+(?:[\\,]\\d{0,2})$")) return true;
            //validating amount with dot separator
        else if(amount.matches("^\\d+(?:[\\.]\\d{0,2})$")) return true;
            //return false if nothing matches the input
        else return false;
    }

}