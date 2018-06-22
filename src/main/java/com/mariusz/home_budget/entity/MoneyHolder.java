package com.mariusz.home_budget.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mariusz.home_budget.helpers.MoneyHolderType;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@MappedSuperclass
@Data
public abstract class MoneyHolder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private MoneyHolderType type;
    @OneToOne
    @JsonIgnore
    private AppUser user;


}
