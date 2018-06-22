package com.mariusz.home_budget.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Expense extends FinanceFlow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JsonIgnore
    private AppUser user;

    @JsonIgnore
    @OneToOne
    private MoneyHolder moneyHolder;
}
