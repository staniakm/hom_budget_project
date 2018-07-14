package com.mariusz.home_budget.repository;

import com.mariusz.home_budget.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface VerificationTokenRepository
        extends JpaRepository<VerificationToken, Long> {

    VerificationToken findByToken(String token);

    void deleteAllByExpiryDateBefore(LocalDateTime date);
}
