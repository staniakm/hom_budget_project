package com.mariusz.home_budget.service;

import com.mariusz.home_budget.entity.*;
import com.mariusz.home_budget.entity.form.MoneyFlowForm;
import com.mariusz.home_budget.entity.form.WalletForm;
import com.mariusz.home_budget.helpers.MoneyHolderType;
import com.mariusz.home_budget.repository.*;
import com.mariusz.home_budget.utils.Validators;
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

        LocalDate operationDate;
        BigDecimal operationAmount;
        Long holderId;

        String date = newOperation.getDate().trim();
        String amount = newOperation.getAmount().replace(",",".").trim();

//        Optional<String> error = Validators.validMoneyFlowOperation(newOperation);


        if (date==null || date.length()==0 ){
            operationDate = LocalDate.now();
        }else {
            try {
                operationDate = LocalDate.parse(date);
            }catch (Exception ex){
                return Optional.of("Date must be valid");
            }
        }

        if (amount==null || amount.length()==0){
            logger.info("Amount must be provided");

            return Optional.of("Amount must be provided");
        }else {
            try {
                operationAmount = new BigDecimal(amount);

            }catch (Exception ex){
                return Optional.of("Amount must be in valid format");
            }
        }

        Optional<AppUser> user = userRepository.findByName(newOperation.getUser());
        if (!user.isPresent()){
            return Optional.of("User details are incorrect. Please login again.");
        }


        if (newOperation.getMoneyHolder()==null || newOperation.getMoneyHolder().trim().length()==0){
            logger.info("Incorrect holder");
            return Optional.of("Incorrect money holder selected.");
        }else {
            try {
                holderId = Long.parseLong(newOperation.getMoneyHolder());
            }catch (Exception ex){
                logger.info("Incorrect holder - exception");
                return Optional.of("Incorrect money holder.");
            }
        }

        Optional<MoneyHolder> moneyHolder = moneyHoldersRepository
                            .findByUserAndId(user.get().getId(),holderId);

        if (!moneyHolder.isPresent()){
            return Optional.of("Incorrect money holder for logged user");
        }

        if (newOperation.getOperation().equalsIgnoreCase("income")){
            Income income = new Income();
            income.setAmount(operationAmount);
            income.setDate(operationDate.atStartOfDay());
            income.setDescription(newOperation.getDescription());
            income.setUser(user.get());
            income.setMoneyHolder(moneyHolder.get());
            moneyHolder.get().addIncome(operationAmount);

            moneyHoldersRepository.save(moneyHolder.get());
            incomeRepository.save(income);
        }else if
            (newOperation.getOperation().equalsIgnoreCase("expense")){
                Expense expense = new Expense();
            expense.setAmount(operationAmount);
            expense.setDate(operationDate.atStartOfDay());
            expense.setDescription(newOperation.getDescription());
            expense.setUser(user.get());
            expense.setMoneyHolder(moneyHolder.get());

            moneyHoldersRepository.save(moneyHolder.get());
            expenseRepository.save(expense);

        }

        return Optional.empty();
    }


    @Override
    public Optional<String> addMoneyHolder(WalletForm walletForm) {

        BigDecimal cashAmount;

        String amount = walletForm.getCash().replace(",",".").trim();
        MoneyHolderType moneyHolderType;

        if (walletForm.getName()==null || walletForm.getName().length()==0){
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
