package com.mariusz.home_budget.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "verification_tokens")
@Data
@NoArgsConstructor
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
        this.expiryDate = calculateDate();
    }

    private LocalDateTime calculateDate(){
        return LocalDateTime.now().plus(EXPIRATION,ChronoUnit.MINUTES);
    }




}
