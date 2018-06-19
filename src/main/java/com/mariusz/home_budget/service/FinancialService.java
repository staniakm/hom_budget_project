package com.mariusz.home_budget.service;


import com.mariusz.home_budget.entity.entity_forms.MoneyFlowForm;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

public interface FinancialService {

    Map<String, BigDecimal> getBalance();

    Optional<String> addOperation(MoneyFlowForm newOperation);
}
