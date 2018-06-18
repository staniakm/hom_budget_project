package com.mariusz.home_budget.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.math.BigDecimal;


@Data
@NoArgsConstructor

public class Balance {

    private int id;
private BigDecimal income;
private BigDecimal expense;

}
