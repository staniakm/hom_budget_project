package com.mariusz.home_budget.service;


import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.PlannedBudget;
import com.mariusz.home_budget.entity.form.BudgetForm;
import com.mariusz.home_budget.mapper.ObjectMapper;
import com.mariusz.home_budget.repository.BudgetRepository;
import com.mariusz.home_budget.repository.FinancialCustomRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class BudgetServiceTest {

    @Autowired
    private BudgetService budgetService;

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private FinancialCustomRepository customRepository;

    @Mock
    private ObjectMapper mapper;


    @Before
    public void setup() {
        budgetService = new BudgetService(budgetRepository, customRepository, mapper);
        AppUser user = new AppUser();
        user.setName("Mariusz");
        user.setId(1L);

        PlannedBudget plannedBudget = new PlannedBudget();
        plannedBudget.setPlanned(BigDecimal.valueOf(100));
        plannedBudget.setSpend(BigDecimal.TEN);
        plannedBudget.setCategory("Samochód");
        plannedBudget.setDate(LocalDate.now());
        plannedBudget.setUser(user);

        PlannedBudget plannedBudget2 = new PlannedBudget();
        plannedBudget2.setPlanned(BigDecimal.valueOf(200));
        plannedBudget2.setSpend(BigDecimal.ONE);
        plannedBudget2.setCategory("Jedzenie");
        plannedBudget2.setDate(LocalDate.now());
        plannedBudget2.setUser(user);

        given(budgetRepository.findAllForCurrentMonth(1L, 2018, 7)).willReturn(Arrays.asList(plannedBudget, plannedBudget2));

        given(budgetRepository.getOneByCategory(1L, "Jedzenie", LocalDate.now().getYear(), LocalDate.now().getMonthValue()))
                .willReturn(plannedBudget2);

        given(budgetRepository.save(plannedBudget2)).willReturn(plannedBudget2);
    }

    @Test
    public void shouldReturnListOfBudgtForProvidedUserAndMonth() {
        AppUser user = new AppUser();
        user.setName("Mariusz");
        user.setId(1L);

        List<PlannedBudget> budgets = budgetService.getPlannedBudgets(user, 7);

        assertThat(budgets.isEmpty()).isEqualTo(false);
        assertThat(budgets.size()).isEqualTo(2);
        assertThat(budgets.get(0).getCategory()).isEqualTo("Samochód");
    }

    @Test
    public void shouldReturnEmptyListIfUserDontHaveBudgetForProvidedMonth() {
        AppUser user = new AppUser();
        user.setName("Mariusz");
        user.setId(1L);
        List<PlannedBudget> budgets = budgetService.getPlannedBudgets(user, 8);
        assertThat(budgets.isEmpty()).isEqualTo(true);
    }


    @Test
    public void shouldReturnEmptyListIfUserHaveNoBudget() {
        AppUser user = new AppUser();
        user.setName("Mariusz");
        user.setId(2L);
        List<PlannedBudget> budgets = budgetService.getPlannedBudgets(user, 7);
        assertThat(budgets.isEmpty()).isEqualTo(true);
    }

    @Test
    public void shouldntBeAbleToSetBudgetWithAmountLessThenZero() {
        AppUser user = new AppUser();
        user.setName("Mariusz");
        user.setId(2L);

        BudgetForm budgetForm = new BudgetForm();
        budgetForm.setAmount("-1");
        budgetForm.setCategory("Samochód");
        budgetForm.setDescription("Samochód");

        Optional<String> result = budgetService.savePlannedBudget(budgetForm, user);

        assertThat(result.isPresent()).isEqualTo(true);
        String error = "";
        if (result.isPresent())
            error = result.get();
        assertThat(error).isEqualTo("Value must be greater then zero");
        verifyZeroInteractions(budgetRepository);
    }

    @Test
    public void shouldBeAbleToUpdateBudgetWithAmountGreaterThenZero() {
        AppUser user = new AppUser();
        user.setName("Mariusz");
        user.setId(2L);

        BudgetForm budgetForm = new BudgetForm();
        budgetForm.setAmount("10");
        budgetForm.setCategory("Samochód");
        budgetForm.setDescription("Samochód");

        PlannedBudget plannedBudget = new PlannedBudget();
        plannedBudget.setUser(user);
        plannedBudget.setPlanned(BigDecimal.TEN);

        given(budgetRepository.save(any(PlannedBudget.class))).willReturn(plannedBudget);

        Optional<String> result = budgetService.savePlannedBudget(budgetForm, user);

        assertThat(result.isPresent()).isEqualTo(false);
    }



    @Test
    public void shouldReturnEmptyOptionalForProperBudget() {
        AppUser user = new AppUser();
        user.setName("Mariusz");
        user.setId(1L);

        BudgetForm budgetForm = new BudgetForm();
        budgetForm.setAmount("10");
        budgetForm.setCategory("Samochód");
        budgetForm.setDescription("Samochód");
        reset(budgetRepository);

        Optional<String> result = budgetService.savePlannedBudget(budgetForm, user);

        assertThat(result.isPresent()).isEqualTo(false);
        verify(budgetRepository, times(1))
                .getOneByCategory(user.getId(), budgetForm.getCategory(), LocalDate.now().getYear(), LocalDate.now().getMonthValue());
        verify(budgetRepository, times(1)).save(mapper.mapToBudget(budgetForm, user, new BigDecimal(budgetForm.getAmount())));

    }

    @Test
    public void updateNotExistingBudgetReturnsNewOptionalBudget() {
        AppUser user = new AppUser();
        user.setName("Mariusz");
        user.setId(1L);
        PlannedBudget budget = null;

        given(budgetRepository.getOneByCategory(1L, "Food", LocalDate.now().getYear(), LocalDate.now().getMonthValue()))
                .willReturn(null);
        given(budgetRepository.save(any(PlannedBudget.class))).willReturn(new PlannedBudget());

        Optional<PlannedBudget> result = budgetService.updateBudget(user, "Food", BigDecimal.TEN);
        assertThat(result.isPresent()).isEqualTo(false);

        reset(budgetRepository);
    }

    @Test
    public void createBudgetReturnsOptionalBudget() {
        AppUser user = new AppUser();
        user.setName("Mariusz");
        user.setId(1L);
        PlannedBudget budget = null;

        Optional<PlannedBudget> result = budgetService.updateBudget(user, "Jedzenie", BigDecimal.TEN);
        assertThat(result.isPresent()).isEqualTo(true);

        if (result.isPresent())
            budget = result.get();

        assertThat(budget).isNotNull();
        assertThat(budget.getCategory()).isEqualTo("Jedzenie");
        assertThat(budget.getPlanned()).isEqualTo(BigDecimal.valueOf(200));

        reset(budgetRepository);

    }

    @Test
    public void updateNotExistingBudgetReturnEmptyOptional() {
        AppUser user = new AppUser();
        user.setName("Mariusz");
        user.setId(1L);
        reset(budgetRepository);

        Optional<PlannedBudget> result = budgetService.updateBudget(user, "Samochód", BigDecimal.TEN);

        assertThat(result.isPresent()).isEqualTo(false);
        verify(budgetRepository, times(1)).getOneByCategory(user.getId(), "Samochód", LocalDate.now().getYear(), LocalDate.now().getMonthValue());
    }


}