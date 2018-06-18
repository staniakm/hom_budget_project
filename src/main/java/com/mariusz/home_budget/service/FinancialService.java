package com.mariusz.home_budget.service;


import java.math.BigDecimal;
import java.util.Map;

public interface FinancialService {

    Map<String, BigDecimal> getBalance();
}
