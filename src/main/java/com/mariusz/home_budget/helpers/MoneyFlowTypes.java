package com.mariusz.home_budget.helpers;

public enum MoneyFlowTypes {
    INCOME("Income"),EXPENSE("Expense");

    private String type;

    MoneyFlowTypes(String type) {

        this.type = type;
    }

    public String getType() {
        return type;
    }
}
