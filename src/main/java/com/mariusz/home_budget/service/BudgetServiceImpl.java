package com.mariusz.home_budget.service;

import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.PlannedBudget;
import com.mariusz.home_budget.entity.form.BudgetForm;
import com.mariusz.home_budget.repository.BudgetRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BudgetServiceImpl implements BudgetService {
    private final BudgetRepository budgetRepository;
    private final ApplicationUserService applicationUserService;

    public BudgetServiceImpl(BudgetRepository budgetRepository, ApplicationUserService applicationUserService) {
        this.budgetRepository = budgetRepository;
        this.applicationUserService = applicationUserService;
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
            budget = new PlannedBudget();
            budget.setUser(user);
            budget.setCategory(budgetForm.getCategory());
            budget.setDate(LocalDate.now());
            budget.setPlanned(operationAmount);
            budget.setSpend(BigDecimal.ZERO);
        }else {
            budget.setPlanned(operationAmount);
        }
        budgetRepository.save(budget);

        return Optional.empty();
    }

    @Override
    public void updateBudget(AppUser user, String category, BigDecimal amount) {
        PlannedBudget budget = budgetRepository.getOneByCategory(user.getId(),category
                ,LocalDate.now().getYear(), LocalDate.now().getMonthValue());
        if (budget!=null){
            budget.setSpend(budget.getSpend().add(amount));
            budgetRepository.save(budget);
        }
    }
}
