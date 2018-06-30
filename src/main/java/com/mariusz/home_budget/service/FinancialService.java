package com.mariusz.home_budget.service;


import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.Expense;
import com.mariusz.home_budget.entity.Income;
import com.mariusz.home_budget.entity.MoneyHolder;
import com.mariusz.home_budget.entity.form.MoneyFlowForm;
import com.mariusz.home_budget.entity.form.WalletForm;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface FinancialService {

    Map<String, BigDecimal> getBalance(Long user_id);

    Optional<String> addOperation(MoneyFlowForm newOperation);

    Optional<String> addMoneyHolder(WalletForm walletForm);

    List<MoneyHolder> getMoneyHolders(AppUser user);

    public void saveIncome(Income income);

    public void saveExpense(Expense expense);

    List<MoneyFlowForm> getMoneyFlows(AppUser user);
}
