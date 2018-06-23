package com.mariusz.home_budget.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mariusz.home_budget.helpers.MoneyFlowTypes;
import com.mariusz.home_budget.helpers.PeriodicTypes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlannedOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MoneyFlowTypes planedType;
    private LocalDate dueDate;
    private String description;
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private PeriodicTypes periodicity;

    @OneToOne
    @JsonIgnore
    private MoneyHolder moneyHolder;
    private int days;

    @OneToOne
    @JsonIgnore
    private AppUser user;

    private boolean isFinished;
    private boolean isActive;

}
