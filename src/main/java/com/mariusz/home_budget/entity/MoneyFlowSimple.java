package com.mariusz.home_budget.entity;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@ToString
public class MoneyFlowSimple {

    private LocalDate date;
    private String description;
    private BigDecimal amount;
    private String operation;


}
