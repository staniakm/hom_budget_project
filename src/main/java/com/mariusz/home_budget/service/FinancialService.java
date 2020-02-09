package com.mariusz.home_budget.service;

import com.mariusz.home_budget.entity.*;
import com.mariusz.home_budget.entity.form.BudgetForm;
import com.mariusz.home_budget.entity.form.MoneyFlowForm;
import com.mariusz.home_budget.entity.form.PlanForm;
import com.mariusz.home_budget.entity.form.WalletForm;
import com.mariusz.home_budget.helpers.MoneyHolderType;
import com.mariusz.home_budget.repository.FinancialRepository;
import com.mariusz.home_budget.validators.Validators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FinancialService {

    private final FinancialRepository financialRepository;
    private final BudgetService budgetService;
    private final InvestmentService investmentService;
    private final MoneyHolderService moneyHolderService;

    private final PlannedService plannedService;
    private final CurrencyService currencyService;

    @Autowired
    public FinancialService(FinancialRepository financialRepository
            , BudgetService budgetService, InvestmentService investmentService, MoneyHolderService moneyHolderService, PlannedService plannedService, CurrencyService currencyService) {
        this.financialRepository = financialRepository;
        this.budgetService = budgetService;
        this.investmentService = investmentService;
        this.moneyHolderService = moneyHolderService;
        this.plannedService = plannedService;
        this.currencyService = currencyService;
    }

    /**
     * Get balance for current month. Returned as map
     *
     * @param user - current logged user
     * @return - map with keys "income","expense","balance"
     */
    public Map<String, BigDecimal> getCurrentMonthAccountBalance(AppUser user) {
        //TODO return new class instead of map
        Balance balance = financialRepository.getBalance(user.getId(), LocalDate.now().getYear(), LocalDate.now().getMonthValue());
        Map<String, BigDecimal> map = new HashMap<>();
        map.put("income", balance.getIncome());
        map.put("expense", balance.getExpense());
        map.put("balance", balance.getIncome().subtract(balance.getExpense()));
        return map;
    }

    /**
     * @param newOperation (expense/income)
     * @param user         (current logged user)
     * @return Optional<String> - if not empty then error occur else values are correct and operation is saved in DB
     */
    public Optional<String> addOperation(MoneyFlowForm newOperation, AppUser user) {
        BigDecimal operationAmount;
        Long holderId;
        LocalDate date = newOperation.getDate();
        String amount = newOperation.getAmount();
        operationAmount = new BigDecimal(amount);


        if (operationAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return Optional.of("Value must be greater then zero");
        }

        Optional<String> error = Validators.validateMoneyHolder(newOperation.getMoneyHolder());
        if (error.isPresent()) {
            return error;
        }

        holderId = Long.parseLong(newOperation.getMoneyHolder());
        Optional<MoneyHolder> moneyHolder = moneyHolderService
                .findByUserAndId(user, holderId);

        if (!moneyHolder.isPresent()) {
            return Optional.of("Incorrect money holder for logged user");
        }

        if (newOperation.getOperation().equalsIgnoreCase("income")) {
            Income income = new Income();
            income.setAmount(operationAmount);
            income.setDate(date.atStartOfDay());
            income.setDescription(newOperation.getDescription());
            income.setUser(user);
            income.setMoneyHolder(moneyHolder.get());
            saveIncome(income);
        } else if
        (newOperation.getOperation().equalsIgnoreCase("expense")) {
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


    /**
     * Save operation "income" in DB
     *
     * @param income - income to savePlannedOperation
     */
    public void saveIncome(Income income) {
        MoneyHolder moneyHolder = income.getMoneyHolder();
        moneyHolder.addIncome(income.getAmount());
        moneyHolderService.save(moneyHolder);
        financialRepository.save(income);
    }

    /**
     * Save operation "expense" in DB
     *
     * @param expense - expanse to savePlannedOperation
     */
    public void saveExpense(Expense expense) {
        MoneyHolder moneyHolder = expense.getMoneyHolder();
        moneyHolder.addExpense(expense.getAmount());
        moneyHolderService.save(moneyHolder);
        financialRepository.save(expense);
    }

    /**
     * Get basic information about operations. Used to display data on frontend form
     *
     * @param user (current logged user)
     * @return List of basic data about operations.
     */
    public List<MoneyFlowSimple> getMoneyFlows(AppUser user) {
        return financialRepository.getMoneyFlows(user);
    }


    /**
     * Delete one of registered operations.
     *
     * @param operationId   - operation id
     * @param user          - current logged user
     * @param operationType - type of operation
     */
    public void deleteMoneyOperation(Long operationId, AppUser user, String operationType) {
        if (operationType.equalsIgnoreCase("income")) {
            Income income = financialRepository.getIncome(user, operationId);
            if (income != null) {
                MoneyHolder moneyHolder = income.getMoneyHolder();
                moneyHolder.setAmount(moneyHolder.getAmount().subtract(income.getAmount()));
                moneyHolderService.save(moneyHolder);
                financialRepository.deleteIncome(income);
            }
        } else if (operationType.equalsIgnoreCase("expense")) {
            Expense expense = financialRepository.getExpense(operationId, user);
            if (expense != null) {
                MoneyHolder moneyHolder = expense.getMoneyHolder();
                moneyHolder.setAmount(moneyHolder.getAmount().add(expense.getAmount()));
                moneyHolderService.save(moneyHolder);
                financialRepository.deleteExpense(expense);
            }
        }

    }


    public List<PlannedBudget> getPlannedBudgets(AppUser user, Integer month) {
        return budgetService.getPlannedBudgets(user, month);
    }

    public List<PlannedOperation> getPlanedActiveOperation(AppUser user) {
        return plannedService.getPlanedActiveOperation(user);
    }

//    @Override
//    public List<Currency> getCurrences() {
//        return currencyService.getCurrences();
//    }

    public List<Currency> getCurrences(AppUser user) {
        return currencyService.getCurrences(user);
    }


    /**
     * Register new money holder
     *
     * @param walletForm - basic information about money holder based on html form.
     * @param user       - current logged user
     * @return - if optional is empty, new money holder was registered successful
     */
    public Optional<String> addMoneyHolder(WalletForm walletForm, AppUser user) {
        //TODO move validation to separate methods
        BigDecimal cashAmount;
        String amount = walletForm.getCash().replace(",", ".").trim();
        MoneyHolderType moneyHolderType;
        if (walletForm.getName() == null || walletForm.getName().trim().length() == 0) {
            return Optional.of("Name must be valid");
        }

        if (walletForm.getMoneyHolderType() == null || walletForm.getMoneyHolderType().trim().length() == 0) {
            return Optional.of("Incorrect type of money holder");
        } else {
            try {
                moneyHolderType = MoneyHolderType.valueOf(walletForm.getMoneyHolderType());
            } catch (IllegalArgumentException ex) {
                return Optional.of("Incorrect type for money holder.");
            }
        }

        if (amount.length() == 0) {
            cashAmount = BigDecimal.ZERO;
        } else {
            try {
                cashAmount = new BigDecimal(amount);
            } catch (Exception ex) {
                return Optional.of("Amount must be in valid format");
            }
        }

        MoneyHolder wallet = new MoneyHolder();
        wallet.setAmount(cashAmount);
        wallet.setName(walletForm.getName());
        wallet.setType(moneyHolderType);
        wallet.setUser(user);
        moneyHolderService.save(wallet);

        return Optional.empty();
    }

    public void finishPlan(Long id, AppUser user) {

        Optional<PlannedOperation> operation = plannedService.findByUserAndIdAndActiveIsTrueAndFinishedIsFalse(user, id);
        if (operation.isPresent()) {
            PlannedOperation plannedOperation = operation.get();
            plannedOperation.setFinished(true);

            if (plannedOperation.getPlanedType().getType().equalsIgnoreCase("income")) {
                Income moneyOperation = new Income();
                moneyOperation.setMoneyHolder(plannedOperation.getMoneyHolder());
                moneyOperation.setUser(plannedOperation.getUser());
                moneyOperation.setDescription(plannedOperation.getDescription());
                moneyOperation.setDate(LocalDateTime.now());
                moneyOperation.setAmount(plannedOperation.getAmount());
                this.saveIncome(moneyOperation);

            } else if (plannedOperation.getPlanedType().getType().equalsIgnoreCase("expense")) {
                Expense moneyOperation = new Expense();
                moneyOperation.setMoneyHolder(plannedOperation.getMoneyHolder());
                moneyOperation.setUser(plannedOperation.getUser());
                moneyOperation.setDescription(plannedOperation.getDescription());
                moneyOperation.setDate(LocalDateTime.now());
                moneyOperation.setAmount(plannedOperation.getAmount());
                this.saveExpense(moneyOperation);
            }

            plannedService.savePlannedOperation(plannedOperation);
        }


    }

    public Optional<String> savePlannedBudget(BudgetForm budgetForm, AppUser user) {
        return budgetService.savePlannedBudget(budgetForm, user);
    }

    public Optional<String> savePlannedOperation(PlanForm planForm, AppUser user) {
        return plannedService.savePlannedOperation(planForm, user);
    }

    public void deletePlan(Long planId, AppUser user) {
        plannedService.deletePlan(planId, user);
    }

    public BigDecimal getInvestmentsSum(AppUser user) {
        return investmentService.getInvestmentsSum(user);
    }
}
