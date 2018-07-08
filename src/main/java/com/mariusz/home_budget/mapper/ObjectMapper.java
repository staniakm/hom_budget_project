package com.mariusz.home_budget.mapper;

import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.Investment;
import com.mariusz.home_budget.entity.PlannedBudget;
import com.mariusz.home_budget.entity.form.BudgetForm;
import com.mariusz.home_budget.entity.form.InvestmentForm;
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

    public Investment mapToInvestment(AppUser user, BigDecimal amount, LocalDate endDate, BigDecimal percentage, InvestmentForm investmentForm) {
        Investment investment = new Investment();
        investment.setUser(user);
        investment.setAmount(amount);
        investment.setEndDate(endDate);
        investment.setStartDate(investmentForm.getDate());
        investment.setPercentage(percentage);
        investment.setLengthDays(investmentForm.getLength());
        investment.setLength(investmentForm.getInvestmentLength());
        investment.setActive(true);
        return investment;
    }
}
