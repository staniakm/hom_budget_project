package com.mariusz.home_budget.service;

import com.mariusz.home_budget.entity.*;
import com.mariusz.home_budget.entity.form.MoneyFlowForm;
import com.mariusz.home_budget.entity.form.WalletForm;
import com.mariusz.home_budget.helpers.MoneyHolderType;
import com.mariusz.home_budget.repository.*;
import com.mariusz.home_budget.validators.Validators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FinancialServiceImpl implements FinancialService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final FinancialCustomRepository financialRepository;
    private final UserRepository userRepository;
    private final MoneyHoldersRepository moneyHoldersRepository;

    @Autowired
    public FinancialServiceImpl(IncomeRepository incomeRepository, ExpenseRepository expenseRepository, FinancialCustomRepository financialRepository, UserRepository userRepository, MoneyHoldersRepository moneyHoldersRepository) {
        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
        this.financialRepository = financialRepository;
        this.userRepository = userRepository;
        this.moneyHoldersRepository = moneyHoldersRepository;
    }


    @Override
    public Map<String, BigDecimal> getBalance(Long id) {

       Balance balance = financialRepository.getBalance(id,LocalDate.now().getYear(),LocalDate.now().getMonthValue());
        Map<String, BigDecimal> map = new HashMap<>();
        map.put("income",balance.getIncome());
        map.put("expense",balance.getExpense());
        map.put("balance",balance.getIncome().subtract(balance.getExpense()));
        return map;


    }

    @Override
    public Optional<String> addOperation(MoneyFlowForm newOperation) {

        BigDecimal operationAmount;
        Long holderId;
        AppUser user = newOperation.getUser();

        LocalDate date = newOperation.getDate();
        String amount = newOperation.getAmount();

        operationAmount = new BigDecimal(amount);

        if (operationAmount.compareTo(BigDecimal.ZERO)<=0){
            return Optional.of("Value must be greater then zero");
        }

        Optional<String> error = Validators.validateMoneyHolder(newOperation.getMoneyHolder());
        if (error.isPresent()){
            return error;
        }

        holderId = Long.parseLong(newOperation.getMoneyHolder());
        Optional<MoneyHolder> moneyHolder = moneyHoldersRepository
                            .findByUserAndId(user.getId(),holderId);

        if (!moneyHolder.isPresent()){
            return Optional.of("Incorrect money holder for logged user");
        }

        if (newOperation.getOperation().equalsIgnoreCase("income")){
            Income income = new Income();
            income.setAmount(operationAmount);
            income.setDate(date.atStartOfDay());
            income.setDescription(newOperation.getDescription());
            income.setUser(user);
            income.setMoneyHolder(moneyHolder.get());
            saveIncome(income);
        }else if
            (newOperation.getOperation().equalsIgnoreCase("expense")){
                Expense expense = new Expense();
            expense.setAmount(operationAmount);
            expense.setDate(date.atStartOfDay());
            expense.setDescription(newOperation.getDescription());
            expense.setUser(user);
            expense.setMoneyHolder(moneyHolder.get());
            saveExpense(expense);
        }

        return Optional.empty();
    }


    public void saveIncome(Income income){

        MoneyHolder moneyHolder = income.getMoneyHolder();
        moneyHolder.addIncome(income.getAmount());

        moneyHoldersRepository.save(moneyHolder);
        incomeRepository.save(income);
    }

    public void saveExpense(Expense expense){
        MoneyHolder moneyHolder = expense.getMoneyHolder();
        moneyHolder.addExpense(expense.getAmount());


        moneyHoldersRepository.save(moneyHolder);
        expenseRepository.save(expense);
    }


    @Override
    public Optional<String> addMoneyHolder(WalletForm walletForm) {

        BigDecimal cashAmount;

        String amount = walletForm.getCash().replace(",",".").trim();
        MoneyHolderType moneyHolderType;

        if (walletForm.getName()==null || walletForm.getName().trim().length()==0){
            logger.info("Name must be valid");
            return Optional.of("Name must be valid");
        }

        if (walletForm.getMoneyHolderType()==null || walletForm.getMoneyHolderType().trim().length()==0){
            return Optional.of("Incorrect type of money holder");
        }else {
            try {
                moneyHolderType = MoneyHolderType.valueOf(walletForm.getMoneyHolderType());
            }catch (IllegalArgumentException ex){
                return Optional.of("Incorrect type for money holder.");
            }
        }

        if (amount==null || amount.length()==0){
            cashAmount = BigDecimal.ZERO;
        }else {
            try {
                cashAmount = new BigDecimal(amount);
            }catch (Exception ex){
                logger.info("Amount must be in valid format");
                return Optional.of("Amount must be in valid format");
            }
        }

        Optional<AppUser> user = userRepository.findByName(walletForm.getUser());
        if (!user.isPresent()){
            return Optional.of("User details are incorrect. Please login again.");
        }

        MoneyHolder wallet = new MoneyHolder();
        wallet.setAmount(cashAmount);
        wallet.setName(walletForm.getName());
        wallet.setType(moneyHolderType);
        wallet.setUser(user.get());
        moneyHoldersRepository.save(wallet);

        return Optional.empty();
    }

    @Override
    public List<MoneyHolder> getMoneyHolders(AppUser user) {
       return moneyHoldersRepository.findAllByUser(user.getId());

    }
}
