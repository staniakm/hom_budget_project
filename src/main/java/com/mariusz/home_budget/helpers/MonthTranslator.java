package com.mariusz.home_budget.helpers;

public enum MonthTranslator {
    JANUARY(1,"month.january")
    ,FEBRUARY(2,"month.february")
    ,MARCH(3,"month.march")
    ,APRIL(4,"month.april")
    ,MAY(5,"month.may")
    ,JUNE(6,"month.june")
    ,JULY(7,"month.july")
    ,AUGUST(8,"month.august")
    ,SEPTEMBER(9,"month.september")
    ,OCTOBER(10,"month.october")
    ,NOVEMBER(11,"month.november")
    ,DECEMBER(12,"month.december");
    private int monthNumber;
    private String month;

    MonthTranslator(int monthNumber, String month) {

        this.monthNumber = monthNumber;
        this.month = month;
    }

    public int getMonthNumber() {
        return monthNumber;
    }

    public String getMonth() {
        return month;
    }
}
