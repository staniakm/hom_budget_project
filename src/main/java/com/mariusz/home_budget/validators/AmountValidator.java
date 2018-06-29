package com.mariusz.home_budget.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AmountValidator implements ConstraintValidator<Amount, String> {

    @Override
    public void initialize(Amount paramA) {
    }

    @Override
    public boolean isValid(String phoneNo, ConstraintValidatorContext ctx) {
        if(phoneNo == null){
            return false;
        }
        //validate phone numbers of format "1234567890"
        if (phoneNo.matches("\\d{10}")) return true;
            //validating phone number with -, . or spaces
        else if(phoneNo.matches("^\\d+(?:\\,\\d{0,2})$")) return true;
            //validating phone number with extension length from 3 to 5
        else if(phoneNo.matches("^\\d+(?:\\.\\d{0,2})$")) return true;
            //validating phone number where area code is in braces ()
            //return false if nothing matches the input
        else return false;
    }

}