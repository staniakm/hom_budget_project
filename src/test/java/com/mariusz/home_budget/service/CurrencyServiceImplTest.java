package com.mariusz.home_budget.service;

import com.mariusz.home_budget.entity.Currency;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;


@RunWith(MockitoJUnitRunner.class)
public class CurrencyServiceImplTest {

    @Autowired
    private CurrencyService currencyService;



    @Before
    public void setUp(){
        currencyService = new CurrencyServiceImpl();
    }

    @Test
    public void shouldReturnListOfUserCurrencies(){



//        List<Currency> userCurrencies = currencyService.getCurrences();




    }



}