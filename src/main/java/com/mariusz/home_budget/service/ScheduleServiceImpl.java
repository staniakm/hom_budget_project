package com.mariusz.home_budget.service;

import com.mariusz.home_budget.entity.Currency;
import com.mariusz.home_budget.repository.FinancialCustomRepository;
import com.mariusz.home_budget.repository.UserRepository;
import com.mariusz.home_budget.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FinancialCustomRepository customRepository;

    @Autowired
    private CurrencyService currencyService;

    /**
     * Clear tokens. Used in scheduler
     */
    @Override
    public void clearTokens() {
        verificationTokenRepository.deleteAllByExpiryDateBefore(LocalDateTime.now());
    }

    @Override
    public void clearInactiveAccounts() {
        userRepository.deleteAllByEnabledFalse();
    }

    @Override
    public void updateCurrencyRate() {
        Map<Long, String> currencies = customRepository.getCurrencies();
        for (Map.Entry<Long, String> mapa : currencies.entrySet()
                ) {
            Currency currency = currencyService.getCurrency(mapa.getValue());
            if (currency != null) {
                LocalDate date = LocalDate.parse(currency.getRates()[0].getEffectiveDate());
                BigDecimal rate = BigDecimal.valueOf(currency.getRates()[0].getMid());
                customRepository.updateCurrencyRate(mapa.getKey(), date, rate);
            }
        }
    }
}
