package com.mariusz.home_budget.service;

import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.Currency;

import java.util.List;

public interface CurrencyService {

    List<Currency> getCurrences();
    List<Currency> getCurrences(AppUser user);
    Currency getCurrency(String link);
}
