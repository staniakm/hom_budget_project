package com.mariusz.home_budget.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "planned_budget")
public class PlannedBudget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String category;
    private LocalDate date;

    private BigDecimal planned;
    private BigDecimal spend;

    @JsonIgnore
    @OneToOne
    private AppUser user;


    public BigDecimal getDifference(){

        return this.planned.subtract(this.spend);
    }

    public BigDecimal getPercent(){
        return (spend.divide(planned,2,BigDecimal.ROUND_HALF_EVEN)).multiply(BigDecimal.valueOf(100));
    }

}
