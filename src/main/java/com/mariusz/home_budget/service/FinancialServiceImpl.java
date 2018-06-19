package com.mariusz.home_budget.service;

import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.Balance;
import com.mariusz.home_budget.entity.Expense;
import com.mariusz.home_budget.entity.Income;
import com.mariusz.home_budget.entity.entity_forms.MoneyFlowForm;
import com.mariusz.home_budget.repository.ExpenseRepository;
import com.mariusz.home_budget.repository.FinancialCustomRepository;
import com.mariusz.home_budget.repository.IncomeRepository;
import com.mariusz.home_budget.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class FinancialServiceImpl implements FinancialService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final FinancialCustomRepository financialRepository;
    private final UserRepository userRepository;

    @Autowired
    public FinancialServiceImpl(IncomeRepository incomeRepository, ExpenseRepository expenseRepository, FinancialCustomRepository financialRepository, UserRepository userRepository) {
        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
        this.financialRepository = financialRepository;
        this.userRepository = userRepository;
    }


    @Override
    public Map<String, BigDecimal> getBalance() {

       Balance balance = financialRepository.getBalance(1,2018,6);
        Map<String, BigDecimal> map = new HashMap<>();
        map.put("income",balance.getIncome());
        map.put("expense",balance.getExpense());
        map.put("balance",balance.getIncome().subtract(balance.getExpense()));
        return map;


    }

    @Override
    public Optional<String> addOperation(MoneyFlowForm newOperation) {

        logger.info("New operation "+newOperation.getOperation());
        LocalDate operationDate = LocalDate.now();
        BigDecimal operationAmount = BigDecimal.ZERO;

        String date = newOperation.getDate().trim();
        String amount = newOperation.getAmount().replace(",",".").trim();


        if (date==null || date.length()==0 ){
            operationDate = LocalDate.now();
        }else {
            try {
                operationDate = LocalDate.parse(date);

            }catch (Exception ex){
                logger.info("Invalid date");

                return Optional.of("Date must be valid");
            }
        }

        if (amount==null || amount.length()==0){
            logger.info("Amount must be provided");

            return Optional.of("Amount must be provided");
        }else {
            try {
                logger.info("Parse string to amount "+amount);

                operationAmount = new BigDecimal(amount);

            }catch (Exception ex){
                logger.info("Amount must be in valid format");
                return Optional.of("Amount must be in valid format");
            }
        }

        Optional<AppUser> user = userRepository.findByName(newOperation.getUser());
        if (!user.isPresent()){
            return Optional.of("User details are incorrect. Please login again.");
        }


        logger.info("operation date: "+ operationDate);
        logger.info("Amount: "+ operationAmount);


        if (newOperation.getOperation().equalsIgnoreCase("income")){
            Income income = new Income();
            income.setAmount(operationAmount);
            income.setDate(operationDate.atStartOfDay());
            income.setDescription(newOperation.getDescription());
            income.setUser(user.get());
            incomeRepository.save(income);
        }else if
            (newOperation.getOperation().equalsIgnoreCase("expense")){
                Expense expense = new Expense();
            expense.setAmount(operationAmount);
            expense.setDate(operationDate.atStartOfDay());
            expense.setDescription(newOperation.getDescription());
            expense.setUser(user.get());
            expenseRepository.save(expense);
            }

        return Optional.empty();
    }
}
