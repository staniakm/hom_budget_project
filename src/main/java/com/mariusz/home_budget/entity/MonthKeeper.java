package com.mariusz.home_budget.entity;

import lombok.Data;

@Data
public class MonthKeeper {

    private String monthName;
    private Integer previous;
    private Integer next;

}
