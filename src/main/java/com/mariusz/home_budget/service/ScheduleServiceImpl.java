package com.mariusz.home_budget.service;

import com.mariusz.home_budget.repository.UserRepository;
import com.mariusz.home_budget.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private UserRepository userRepository;

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
}
