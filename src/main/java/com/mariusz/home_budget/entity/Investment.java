package com.mariusz.home_budget.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mariusz.home_budget.helpers.LengthKeeper;
import lombok.Data;
import org.joda.time.Days;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Duration;
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
    @Column(name = "length_multiplier")
    private int lengthDays;

    @Enumerated(value = EnumType.STRING)
    private LengthKeeper length;

    @Column(name = "is_active")
    private boolean isActive;

    public Long calculateTillEnd(){
        Duration duration = Duration.between(LocalDate.now().atStartOfDay(), endDate.atStartOfDay());
        long diff = Math.abs(duration.toDays());
        return diff;
    }

}
