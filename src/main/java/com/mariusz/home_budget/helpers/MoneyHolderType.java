package com.mariusz.home_budget.helpers;

public enum MoneyHolderType {
    WALLET("Wallet"),BANK_ACCOUNT("Bank account"),CARD("Credit card");

    private final String type;

    MoneyHolderType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
