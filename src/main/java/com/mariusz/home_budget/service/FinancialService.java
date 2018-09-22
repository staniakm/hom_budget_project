package com.mariusz.home_budget.service;


import com.mariusz.home_budget.entity.*;
import com.mariusz.home_budget.entity.form.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface FinancialService {

    Map<String, BigDecimal> getCurrentMonthAccountBalance(AppUser user);

    Optional<String> addOperation(MoneyFlowForm newOperation, AppUser user);

    Optional<String> addMoneyHolder(WalletForm walletForm, AppUser user);

    void saveIncome(Income income);

    void saveExpense(Expense expense);

    List<MoneyFlowSimple> getMoneyFlows(AppUser user);

    void deleteMoneyOperation(Long id, AppUser user, String operationType);

    List<PlannedBudget> getPlannedBudgets(AppUser user, Integer month);

    List<PlannedOperation> getPlanedActiveOperation(AppUser user);

//    List<Currency> getCurrences();

    List<Currency> getCurrences(AppUser user);

    void finishPlan(Long id, AppUser user);

    Optional<String> savePlannedBudget(BudgetForm budgetForm, AppUser user);

    Optional<String> savePlannedOperation(PlanForm planForm, AppUser user);

    void deletePlan(Long id, AppUser user);

    BigDecimal getInvestmentsSum(AppUser user);

}
