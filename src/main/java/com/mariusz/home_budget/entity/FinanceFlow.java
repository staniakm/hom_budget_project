package com.mariusz.home_budget.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
abstract class FinanceFlow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JsonIgnore
    private AppUser user;

    @JsonIgnore
    @OneToOne
    private MoneyHolder moneyHolder;

    private String category;

    private LocalDateTime date;
    private String description;
    private BigDecimal amount;

}
