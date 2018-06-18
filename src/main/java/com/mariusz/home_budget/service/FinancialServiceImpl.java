package com.mariusz.home_budget.service;

import com.mariusz.home_budget.entity.Balance;
import com.mariusz.home_budget.repository.FinancialRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class FinancialServiceImpl implements FinancialService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final FinancialRepository financialRepository;

    @Autowired
    public FinancialServiceImpl(FinancialRepository financialRepository) {
        this.financialRepository = financialRepository;
    }


    @Override
    public Map<String, BigDecimal> getBalance() {

       Balance balance = financialRepository.getBalance(1,2018,6);
//        logger.info(balance.getExpense()+"");
//        logger.info(balance.getIncome()+"");
        Map<String, BigDecimal> map = new HashMap<>();
        map.put("income",balance.getIncome());
        map.put("expense",balance.getExpense());
        map.put("balance",balance.getIncome().subtract(balance.getExpense()));
        return map;


    }
}
