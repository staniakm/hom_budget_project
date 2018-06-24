package com.mariusz.home_budget.entity.form;

import lombok.Data;

@Data
public class MoneyFlowForm {
    private String date;
    private String description;
    private String amount;
    private String operation;
    private String user;
    private String moneyHolder;

    @Override
    public String toString() {
        return "MoneyFlowForm{" +
                "date=" + date +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                ", operation='" + operation + '\'' +
                '}';
    }
}
