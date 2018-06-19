package com.mariusz.home_budget.entity.entity_forms;

import lombok.Data;

@Data
public class MoneyFlowForm {
    private String date;
    private String description;
    private String amount;
    private String operation;
    private String user;

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
