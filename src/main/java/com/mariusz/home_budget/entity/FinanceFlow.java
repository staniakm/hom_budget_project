package com.mariusz.home_budget.entity;


import lombok.Data;

import javax.persistence.MappedSuperclass;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
public abstract class FinanceFlow {

    private LocalDateTime date;
    private String description;
    private BigDecimal amount;

}
