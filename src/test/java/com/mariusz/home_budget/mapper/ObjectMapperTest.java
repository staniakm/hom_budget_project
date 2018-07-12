package com.mariusz.home_budget.mapper;

import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.PlannedBudget;
import com.mariusz.home_budget.entity.form.BudgetForm;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class ObjectMapperTest {

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void mapToBudget() {
        AppUser user = new AppUser();
        user.setId(1L);
        user.setName("Mariusz");
        user.setPassword("secret");
        user.setEnabled(true);

        BudgetForm form = new BudgetForm();
        form.setDescription("Opis");
        form.setCategory("Jedzenie");
        form.setAmount("10");

        PlannedBudget budget = mapper.mapToBudget(form,user,new BigDecimal(form.getAmount()));

        assertThat(budget.getPlanned()).isEqualTo(new BigDecimal(form.getAmount()));
        assertThat(budget.getCategory()).isEqualTo(form.getCategory());
        assertThat(budget.getSpend()).isEqualTo(BigDecimal.ZERO);
        assertThat(budget.getUser()).isEqualTo(user);
        assertThat(budget.getDate()).isEqualTo(LocalDate.now());
        assertThat(budget.getDifference()).isEqualTo(BigDecimal.TEN);
        assertThat(budget.getPercent()).isEqualTo(BigDecimal.valueOf(0.00).setScale(2,RoundingMode.HALF_DOWN));
    }

    @Test
    public void mapToInvestment() {
    }
}