package com.mariusz.home_budget.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mariusz.home_budget.helpers.LengthKeeper;
import lombok.Data;
import org.joda.time.Days;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;

@Entity
@Data
public class Investment {

    private static final BigDecimal BELKA_TAX = BigDecimal.valueOf(0.81);
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

    public String calculateTillEnd(){
        if (endDate.isBefore(LocalDate.now())){
            return "Zakończona";
        }
        Duration duration = Duration.between(LocalDate.now().atStartOfDay(), endDate.atStartOfDay());
        long diff = Math.abs(duration.toDays());
        return ""+diff+" dni";
    }

    public BigDecimal calculatedIncome(){
        //income at the end of investment
        BigDecimal percentRange = getPercentagePerDay(percentage, (long) (lengthDays * length.getDays()));
        percentRange = percentRange.divide(BigDecimal.valueOf(100),6,BigDecimal.ROUND_HALF_DOWN);
        return amount.multiply(percentRange).multiply(BELKA_TAX).setScale(2,RoundingMode.HALF_DOWN);
    }

    public BigDecimal currentIncome(){
        //income at this moment
        if (endDate.isBefore(LocalDate.now())){
            return calculatedIncome();
        }
        Duration duration = Duration.between(LocalDate.now().atStartOfDay(), startDate.atStartOfDay());
        long diff = Math.abs(duration.toDays());

        BigDecimal percentRange = getPercentagePerDay(percentage,diff);
        percentRange = percentRange.divide(BigDecimal.valueOf(100),6,BigDecimal.ROUND_HALF_DOWN);
        return amount.multiply(percentRange).multiply(BELKA_TAX).setScale(2,RoundingMode.HALF_DOWN);
    }

    private static BigDecimal getPercentagePerDay(BigDecimal percentage, Long days){
        return percentage.divide(BigDecimal.valueOf(365),8,BigDecimal.ROUND_HALF_DOWN).multiply(BigDecimal.valueOf(days));
    }
}
