package com.mariusz.home_budget.helpers;

public enum LengthKeeper {
    DAY(1, "length.day"), MONTH(30, "length.month"),YEAR(365,"length.year");

    private final int days;
    private final String message;

    LengthKeeper(int days, String message) {

        this.days = days;
        this.message = message;
    }

    public int getDays() {
        return days;
    }

    public String getMessage() {
        return message;
    }
}
