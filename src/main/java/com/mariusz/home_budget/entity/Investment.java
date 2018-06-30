package com.mariusz.home_budget.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mariusz.home_budget.helpers.LengthKeeper;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class Investment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    @OneToOne
    private AppUser user;

    @Column(name = "start_date")
    private LocalDate startDate;
    private BigDecimal amount;
    private BigDecimal percentage;
    @Column(name = "end_date")
    private LocalDate endDate;
    @Column(name = "length_days")
    private int lengthDays;

    @Enumerated(value = EnumType.STRING)
    private LengthKeeper length;

    private boolean isActive;



}
