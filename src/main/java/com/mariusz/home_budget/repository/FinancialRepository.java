package com.mariusz.home_budget.repository;

import com.mariusz.home_budget.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

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

    public List<Investment> getInvestments(AppUser user) {
        return investmentRepository.findAllByUserAndActiveTrue(user);
    }

    public Investment getInvestmentsById(AppUser user, Long id) {
        return investmentRepository.findByUserAndId(user,id);
//        return investmentRepository.getInvestmentById(user.getId(), id);
    }

    public List<MoneyFlowSimple> getMoneyFlows(AppUser user) {
        return customRepository.getMoneyFlow(user.getId(),LocalDate.now().getYear(), LocalDate.now().getMonthValue());

    }

    public void deleteIncome(Income income) {
        incomeRepository.delete(income);
    }

    public void deleteExpense(Expense expense) {
        expenseRepository.delete(expense);
    }

    public Income getIncome( AppUser user,Long operationId) {
       return incomeRepository.findByUserAndId(user,operationId);
    }

    public Expense getExpense(Long operationId, AppUser user) {
        return expenseRepository.findByUserAndId(user,operationId);
    }

    public void clearTokens() {
        customRepository.clearTokens();
    }
}
