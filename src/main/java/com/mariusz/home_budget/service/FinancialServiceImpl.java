package com.mariusz.home_budget.service;

import com.mariusz.home_budget.entity.*;
import com.mariusz.home_budget.entity.form.InvestmentForm;
import com.mariusz.home_budget.entity.form.MoneyFlowForm;
import com.mariusz.home_budget.entity.form.WalletForm;
import com.mariusz.home_budget.helpers.MoneyHolderType;
import com.mariusz.home_budget.repository.FinancialRepository;
import com.mariusz.home_budget.repository.MoneyHoldersRepository;
import com.mariusz.home_budget.validators.Validators;
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

    private final FinancialRepository financialRepository;
    private final MoneyHoldersRepository moneyHoldersRepository;
    private final BudgetService budgetService;

    @Autowired
    public FinancialServiceImpl(FinancialRepository financialRepository
            , MoneyHoldersRepository moneyHoldersRepository
            , BudgetService budgetService)
    {
        this.financialRepository = financialRepository;
        this.moneyHoldersRepository = moneyHoldersRepository;
        this.budgetService = budgetService;
    }


    @Override
    public Map<String, BigDecimal> getCurrentMonthAccountBalance(Long id) {

        Balance balance = financialRepository.getBalance(id,LocalDate.now().getYear(),LocalDate.now().getMonthValue());
        Map<String, BigDecimal> map = new HashMap<>();
        map.put("income",balance.getIncome());
        map.put("expense",balance.getExpense());
        map.put("balance",balance.getIncome().subtract(balance.getExpense()));
        return map;
    }

//    @Override
//    public Optional<String> addOperation(MoneyFlowForm newOperation, AppUser user) {
//        return Optional.empty();
//    }

    @Override
    public Optional<String> addOperation(MoneyFlowForm newOperation, AppUser user) {

        BigDecimal operationAmount;
        Long holderId;

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
            expense.setCategory(newOperation.getCategory());
            budgetService.updateBudget(user, newOperation.getCategory(), operationAmount);


            saveExpense(expense);
        }

        return Optional.empty();
    }


    public void saveIncome(Income income){
        MoneyHolder moneyHolder = income.getMoneyHolder();
        moneyHolder.addIncome(income.getAmount());
        moneyHoldersRepository.save(moneyHolder);
        financialRepository.save(income);
    }

    public void saveExpense(Expense expense){
        MoneyHolder moneyHolder = expense.getMoneyHolder();
        moneyHolder.addExpense(expense.getAmount());
        moneyHoldersRepository.save(moneyHolder);
        financialRepository.save(expense);
    }

    @Override
    public List<MoneyFlowSimple> getMoneyFlows(AppUser user) {
        return financialRepository.getMoneyFlows(user);
    }

    @Override
    public Optional<String> addInvestment(InvestmentForm investmentForm, AppUser user) {

        BigDecimal amount = new BigDecimal(investmentForm.getAmount());
        LocalDate endDate = investmentForm.getDate().plusDays(investmentForm.getInvestmentLength().getDays()*investmentForm.getLength());
        BigDecimal percentage = new BigDecimal(investmentForm.getPercentage());

        if (amount.compareTo(BigDecimal.ZERO)<=0){
            return Optional.of("Value can't be zero or les then zero");
        }

        if (percentage.compareTo(BigDecimal.ZERO)<=0){
            return Optional.of("Percentage can't be zero or les then zero");
        }

        Investment investment = new Investment();
        investment.setUser(user);
        investment.setAmount(amount);
        investment.setEndDate(endDate);
        investment.setStartDate(investmentForm.getDate());
        investment.setPercentage(percentage);
        investment.setLengthDays(investmentForm.getLength());
        investment.setLength(investmentForm.getInvestmentLength());
        investment.setActive(true);

        financialRepository.save(investment);

        return Optional.empty();
    }

    @Override
    public List<Investment> getInvestments(AppUser user) {
        return financialRepository.getInvestments(user);
    }

    @Override
    public List<Investment> getInvestmentsById(AppUser user, Long id) {
        return financialRepository.getInvestmentsById(user, id);
    }

    @Override
    public void recalculateBudget(AppUser user) {
        financialRepository.recalculateBudget(user.getId(), LocalDate.now().getYear(), LocalDate.now().getMonthValue());
    }

    @Override
    public BigDecimal getTotalAmount(AppUser user) {
        BigDecimal amount = moneyHoldersRepository.findAllByUser(user.getId()).stream().map(MoneyHolder::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
        return amount==null?BigDecimal.ZERO:amount;
    }

    @Override
    public void deleteMoneyOperation(Long id, AppUser user, String operationType) {
        if (operationType.equalsIgnoreCase("income")){
            Income income =financialRepository.getIncome(id, user.getId());
            if (income!=null){
                MoneyHolder moneyHolder = income.getMoneyHolder();
                moneyHolder.setAmount(moneyHolder.getAmount().subtract(income.getAmount()));
                moneyHoldersRepository.save(moneyHolder);
                financialRepository.deleteIncome(income);
            }
        }else if (operationType.equalsIgnoreCase("expense")){
            Expense expense =financialRepository.getExpense(id, user.getId());
            if (expense!=null){
                MoneyHolder moneyHolder = expense.getMoneyHolder();
                moneyHolder.setAmount(moneyHolder.getAmount().add(expense.getAmount()));
                moneyHoldersRepository.save(moneyHolder);
                financialRepository.deleteExpense(expense);
            }
        }

    }

    @Override
    public BigDecimal getInvestmentsSum(AppUser user) {
      return getInvestments(user).stream().map(Investment::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    @Override
    public void clearTokens() {
        financialRepository.clearTokens();
    }


    @Override
    public Optional<String> addMoneyHolder(WalletForm walletForm, AppUser user) {
        BigDecimal cashAmount;
        String amount = walletForm.getCash().replace(",",".").trim();
        MoneyHolderType moneyHolderType;
        if (walletForm.getName()==null || walletForm.getName().trim().length()==0){
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
                return Optional.of("Amount must be in valid format");
            }
        }

        MoneyHolder wallet = new MoneyHolder();
        wallet.setAmount(cashAmount);
        wallet.setName(walletForm.getName());
        wallet.setType(moneyHolderType);
        wallet.setUser(user);
        moneyHoldersRepository.save(wallet);

        return Optional.empty();
    }

    @Override
    public List<MoneyHolder> getMoneyHolders(AppUser user) {
       return moneyHoldersRepository.findAllByUser(user.getId());

    }
}
