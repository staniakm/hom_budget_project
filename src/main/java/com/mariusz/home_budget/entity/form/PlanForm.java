package com.mariusz.home_budget.entity.form;


import lombok.Data;

@Data
public class PlanForm {
    private String planedType;
    private String dueDate;
    private String description;
    private String amount;
    private String periodicity;
    private String moneyHolder;

}
