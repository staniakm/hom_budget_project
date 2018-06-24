package com.mariusz.home_budget.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mariusz.home_budget.helpers.MoneyHolderType;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "money_container")
@Data
public class MoneyHolder {

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

    public void addIncome(BigDecimal income){
        this.amount = amount.add(income);
    }

    public void addExpense(BigDecimal expense){
        this.amount = amount.subtract(expense);
    }
}
