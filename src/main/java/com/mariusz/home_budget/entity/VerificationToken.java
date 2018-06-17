package com.mariusz.home_budget.entity;

import com.mariusz.home_budget.entity.AppUser;
import lombok.Data;

import javax.annotation.Generated;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

@Entity
@Table(name = "verification_tokens")
@Data
public class VerificationToken {
    private static final int EXPIRATION = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne(targetEntity = AppUser.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private AppUser user;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    public VerificationToken(String token, AppUser user) {

        this.token = token;
        this.user = user;
        this.expiryDate = calculateDate(EXPIRATION);
    }

    public VerificationToken() {
        super();
    }

    public VerificationToken(final String token) {
        super();

        this.token = token;
        this.expiryDate = calculateDate(EXPIRATION);
    }

    private LocalDateTime calculateDate(int expiryTimeInMinutes){
        return LocalDateTime.now().plus(expiryTimeInMinutes,ChronoUnit.MINUTES);
    }




}
