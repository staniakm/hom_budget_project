package com.mariusz.home_budget.service;

import com.mariusz.home_budget.entity.Currency;
import com.mariusz.home_budget.repository.FinancialCustomRepository;
import com.mariusz.home_budget.repository.UserRepository;
import com.mariusz.home_budget.repository.VerificationTokenRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class ScheduleService {

    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;
    private final FinancialCustomRepository customRepository;
    private final CurrencyService currencyService;

    public ScheduleService(VerificationTokenRepository verificationTokenRepository, UserRepository userRepository, FinancialCustomRepository customRepository, CurrencyService currencyService) {
        this.verificationTokenRepository = verificationTokenRepository;
        this.userRepository = userRepository;
        this.customRepository = customRepository;
        this.currencyService = currencyService;
    }

    /**
     * Clear tokens. Used in scheduler
     */

    public void clearTokens() {
        verificationTokenRepository.deleteAllByExpiryDateBefore(LocalDateTime.now());
    }


    public void clearInactiveAccounts() {
        userRepository.deleteAllByEnabledFalse();
    }


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
