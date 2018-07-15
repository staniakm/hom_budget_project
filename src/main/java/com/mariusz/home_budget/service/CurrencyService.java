package com.mariusz.home_budget.service;

import com.mariusz.home_budget.entity.Currency;

import java.util.List;

public interface CurrencyService {

    List<Currency> getCurrences();
    Currency getCurrency(String link);
}
