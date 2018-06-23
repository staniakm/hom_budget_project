package com.mariusz.home_budget.helpers;

public enum  PeriodicTypes {
    ONCE(0,"Once"),DAILY(1,"Every day"), WEEKLY(7,"Every week"), EVERY_2_WEEKS(14,"Every 2 weeks"), MONTHLY(30,"Monthly")
    ;

    private final int days;
    private final String description;

    PeriodicTypes(int days, String description) {

        this.days = days;
        this.description = description;
    }

    public int getDays() {
        return days;
    }

    public String getDescription() {
        return description;
    }
}
