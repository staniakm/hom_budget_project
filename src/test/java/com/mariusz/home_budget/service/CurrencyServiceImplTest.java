package com.mariusz.home_budget.service;

import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.Currency;
import com.mariusz.home_budget.repository.FinancialCustomRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class CurrencyServiceImplTest {

    @Autowired
    private CurrencyService currencyService;

    @Mock
    private FinancialCustomRepository customRepository;


    @Before
    public void setUp(){
        currencyService = new CurrencyServiceImpl(customRepository);
    }

    @Test
    public void shouldReturnListOfUserCurrencies(){
        AppUser user = new AppUser();
        user.setName("Mariusz");
        user.setId(1L);

        Currency currencyUsd = new Currency();
        currencyUsd.setCurrency("USD");
        Currency currencyEur = new Currency();
        currencyEur.setCurrency("EUR");

        given(customRepository.getCurrenciesId(user)).willReturn(Arrays.asList("1","2"));
        given(customRepository.getListOfCurencies(any(List.class))).willReturn(Arrays.asList(currencyEur, currencyUsd));

        List<Currency> userCurrencies = currencyService.getCurrences(user);

        assertThat(userCurrencies.size()).isEqualTo(2);
        assertThat(userCurrencies.contains(currencyEur)).isTrue();
        assertThat(userCurrencies.contains(currencyUsd)).isTrue();
    }


    @Test
    public void shouldReturnEmptyListForUserWithoutCurrencies(){
        AppUser user = new AppUser();
        user.setName("Mariusz");
        user.setId(1L);

        Currency currencyUsd = new Currency();
        currencyUsd.setCurrency("USD");
        Currency currencyEur = new Currency();
        currencyEur.setCurrency("EUR");

        given(customRepository.getCurrenciesId(user)).willReturn(Collections.emptyList());

        List<Currency> userCurrencies = currencyService.getCurrences(user);

        assertThat(userCurrencies.isEmpty()).isTrue();
    }



}