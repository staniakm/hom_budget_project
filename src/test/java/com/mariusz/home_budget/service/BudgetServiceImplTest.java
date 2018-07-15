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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class BudgetServiceImplTest {


    @TestConfiguration
    static class EmployeeServiceImplContextConfiguration{
        private BudgetRepository budgetRepository = mock(BudgetRepository.class);
        private ObjectMapper mapper = new ObjectMapper();
        private FinancialCustomRepository customRepository = mock(FinancialCustomRepository.class);
        @Bean
        public BudgetService budgetService(){

            return new BudgetServiceImpl(budgetRepository, customRepository, mapper);
        }

        @Bean BudgetRepository budgetRepository(){
            return budgetRepository;
        }
        @Bean ObjectMapper mapper() {return mapper;}
    }

    @Autowired
    private BudgetService budgetService;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private ObjectMapper mapper;


    @Before
public void setup(){
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

    when(budgetRepository.findAllForCurrentMonth(1L,2018,7)).thenReturn(Arrays.asList(plannedBudget, plannedBudget2));

    when(budgetRepository.getOneByCategory(1L,"Samochód",LocalDate.now().getYear(), LocalDate.now().getMonthValue())).thenReturn(null);

    when(budgetRepository.getOneByCategory(1L,"Jedzenie",LocalDate.now().getYear(), LocalDate.now().getMonthValue())).thenReturn(plannedBudget2);

    when(budgetRepository.save(plannedBudget2)).thenReturn(plannedBudget2);
}

    @Test
    public void shouldReturnListOfBudgtForProvidedUserAndMonth(){
        AppUser user = new AppUser();
        user.setName("Mariusz");
        user.setId(1L);

        List<PlannedBudget> budgets = budgetService.getPlannedBudgets(user,7);

        assertThat(budgets.isEmpty()).isEqualTo(false);
        assertThat(budgets.size()).isEqualTo(2);
        assertThat(budgets.get(0).getCategory()).isEqualTo("Samochód");
    }

    @Test
    public void shouldReturnEmptyListIfUserDontHaveBudgetForProvidedMonth(){
        AppUser user = new AppUser();
        user.setName("Mariusz");
        user.setId(1L);
        List<PlannedBudget> budgets = budgetService.getPlannedBudgets(user,8);
        assertThat(budgets.isEmpty()).isEqualTo(true);
    }


    @Test
    public void shouldReturnEmptyListIfUserHaveNoBudget(){
        AppUser user = new AppUser();
        user.setName("Mariusz");
        user.setId(2L);
        List<PlannedBudget> budgets = budgetService.getPlannedBudgets(user,7);
        assertThat(budgets.isEmpty()).isEqualTo(true);
    }

    @Test
    public void shouldntBeAbleToSetBudgetWithAmountLessThenZero(){
        AppUser user = new AppUser();
        user.setName("Mariusz");
        user.setId(2L);

        BudgetForm budgetForm = new BudgetForm();
        budgetForm.setAmount("-1");
        budgetForm.setCategory("Samochód");
        budgetForm.setDescription("Samochód");

        Optional<String> result = budgetService.savePlannedBudget(budgetForm,user);

        assertThat(result.isPresent()).isEqualTo(true);
        String error ="";
        if (result.isPresent())
            error = result.get();
        assertThat(error).isEqualTo("Value must be greater then zero");
        verifyZeroInteractions(budgetRepository);
    }


    @Test
    public void shouldReturnEmptyOptionalForProperBudget(){
        AppUser user = new AppUser();
        user.setName("Mariusz");
        user.setId(1L);

        BudgetForm budgetForm = new BudgetForm();
        budgetForm.setAmount("10");
        budgetForm.setCategory("Samochód");
        budgetForm.setDescription("Samochód");
        reset(budgetRepository);

        Optional<String> result = budgetService.savePlannedBudget(budgetForm,user);

        assertThat(result.isPresent()).isEqualTo(false);
        verify(budgetRepository,times(1))
                .getOneByCategory(user.getId(),budgetForm.getCategory(),LocalDate.now().getYear(),LocalDate.now().getMonthValue());
        verify(budgetRepository,times(1)).save(mapper.mapToBudget(budgetForm,user,new BigDecimal(budgetForm.getAmount())));

    }

    @Test
    public void updateExistingBudgetReturnsOptionalBudget(){
        AppUser user = new AppUser();
        user.setName("Mariusz");
        user.setId(1L);
        PlannedBudget budget=null;

        Optional<PlannedBudget> result = budgetService.updateBudget(user,"Jedzenie",BigDecimal.TEN);
        assertThat(result.isPresent()).isEqualTo(true);

        if (result.isPresent())
            budget = result.get();

        assertThat(budget).isNotNull();
        assertThat(budget.getCategory()).isEqualTo("Jedzenie");
        assertThat(budget.getPlanned()).isEqualTo(BigDecimal.valueOf(200));

        reset(budgetRepository);

    }

    @Test
    public void updateNotExistingBudgetReturnEmptyOptional(){
        AppUser user = new AppUser();
        user.setName("Mariusz");
        user.setId(1L);
        reset(budgetRepository);

        Optional<PlannedBudget> result = budgetService.updateBudget(user,"Samochód",BigDecimal.TEN);

        assertThat(result.isPresent()).isEqualTo(false);
        verify(budgetRepository,times(1)).getOneByCategory(user.getId(),"Samochód",LocalDate.now().getYear(),LocalDate.now().getMonthValue());
    }



}