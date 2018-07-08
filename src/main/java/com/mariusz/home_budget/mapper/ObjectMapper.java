package com.mariusz.home_budget.mapper;

import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.PlannedBudget;
import com.mariusz.home_budget.entity.form.BudgetForm;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class ObjectMapper {

    public String greet(){
        return "Mariusz";
    }

    public PlannedBudget mapToBudget(BudgetForm budgetForm, AppUser user, BigDecimal operationAmount ){
       PlannedBudget budget = new PlannedBudget();
        budget.setUser(user);
        budget.setCategory(budgetForm.getCategory());
        budget.setDate(LocalDate.now());
        budget.setPlanned(operationAmount);
        budget.setSpend(BigDecimal.ZERO);
        return budget;
    }

}
