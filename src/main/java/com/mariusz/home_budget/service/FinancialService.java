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

    List<MoneyHolder> getMoneyHolders(AppUser user);

    void saveIncome(Income income);

    void saveExpense(Expense expense);

    List<MoneyFlowSimple> getMoneyFlows(AppUser user);

    Optional<String> addInvestment(InvestmentForm investmentForm, AppUser user);

    List<Investment> getInvestments(AppUser user);

    Investment getInvestmentsById(AppUser user, Long id);

    void recalculateBudget(AppUser user);

    BigDecimal getTotalAmount(AppUser user);

    void deleteMoneyOperation(Long id, AppUser user, String operationType);

    BigDecimal getInvestmentsSum(AppUser user);

    List<PlannedBudget> getPlannedBudgets(AppUser user, Integer month);

    List<PlannedOperation> getPlanedActiveOperation(AppUser user);

    List<Currency> getCurrences();

    void finishPlan(Long id, AppUser user);

    Optional<String> savePlannedBudget(BudgetForm budgetForm, AppUser user);

    Optional<String> savePlannedOperation(PlanForm planForm, AppUser user);

    void deletePlan(Long id, AppUser user);

}
