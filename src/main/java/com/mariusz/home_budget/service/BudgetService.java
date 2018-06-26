package com.mariusz.home_budget.service;

import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.PlannedBudget;
import com.mariusz.home_budget.entity.form.BudgetForm;

import java.util.List;
import java.util.Optional;

public interface BudgetService {
    List<PlannedBudget> getPlannedBudgets(AppUser user);

    Optional<String> savePlannedBudget(BudgetForm budgetForm, String name);
}
