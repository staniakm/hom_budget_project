package com.mariusz.home_budget.service;

import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.PlannedBudget;
import com.mariusz.home_budget.entity.form.BudgetForm;
import com.mariusz.home_budget.mapper.ObjectMapper;
import com.mariusz.home_budget.repository.BudgetRepository;
import com.mariusz.home_budget.repository.FinancialCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BudgetServiceImpl implements BudgetService {
    private final BudgetRepository budgetRepository;
    private final FinancialCustomRepository customRepository;

    private final ObjectMapper mapper;

    @Autowired
    public BudgetServiceImpl(BudgetRepository budgetRepository, FinancialCustomRepository customRepository, ObjectMapper mapper) {
        this.budgetRepository = budgetRepository;
        this.customRepository = customRepository;
        this.mapper = mapper;
    }


    @Override
    public List<PlannedBudget> getPlannedBudgets(AppUser user, Integer month) {

        return budgetRepository.findAllForCurrentMonth(user.getId(), LocalDate.now().getYear(), month);
    }

    @Override
    public Optional<String> savePlannedBudget(BudgetForm budgetForm, AppUser user) {
        String amount = budgetForm.getAmount();
        BigDecimal operationAmount = new BigDecimal(amount);

        if (operationAmount.compareTo(BigDecimal.ZERO)<0){
            return Optional.of("Value must be greater then zero");
        }

        PlannedBudget budget = budgetRepository.getOneByCategory(user.getId()
                , budgetForm.getCategory(),LocalDate.now().getYear(), LocalDate.now().getMonthValue() );
        if (budget==null) {
            budget = mapper.mapToBudget(budgetForm,user,operationAmount);
        }else {
            budget.setPlanned(operationAmount);
        }
        budgetRepository.save(budget);

        return Optional.empty();
    }

    @Override
    public Optional<PlannedBudget> updateBudget(AppUser user, String category, BigDecimal amount) {
        PlannedBudget budget = budgetRepository.getOneByCategory(user.getId(),category
                ,LocalDate.now().getYear(), LocalDate.now().getMonthValue());
        if (budget!=null){
            budget.setSpend(budget.getSpend().add(amount));
            return Optional.of(budgetRepository.save(budget));
        }
        return Optional.empty();
    }

    /**
     * Recalculate budgets for current logged user for current month.
     * @param user (current logged user)
     */
    @Override
    public void recalculateBudget(AppUser user) {
        //TODO change to allow recalculate budgets for other months
        customRepository.recalculateBudgets(user.getId(), LocalDate.now().getYear(), LocalDate.now().getMonthValue());
    }
}
