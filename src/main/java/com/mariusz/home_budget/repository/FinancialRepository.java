package com.mariusz.home_budget.repository;

import com.mariusz.home_budget.entity.Balance;
import com.mariusz.home_budget.entity.Expense;
import com.mariusz.home_budget.entity.Income;
import com.mariusz.home_budget.entity.Investment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FinancialRepository {

    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final FinancialCustomRepository customRepository;
    private final InvestmentRepository investmentRepository;
    @Autowired
    public FinancialRepository(IncomeRepository incomeRepository
            , ExpenseRepository expenseRepository
            , FinancialCustomRepository customRepository, InvestmentRepository investmentRepository) {
        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
        this.customRepository = customRepository;
        this.investmentRepository = investmentRepository;
    }


    public Balance getBalance(Long id, int year, int monthValue) {
        return customRepository.getBalance(id, year,monthValue);

    }

    public void save(Income income) {
        incomeRepository.save(income);
    }

    public void save(Expense expense) {
        expenseRepository.save(expense);
    }

    public void save(Investment investment){
        investmentRepository.save(investment);
    }
}
