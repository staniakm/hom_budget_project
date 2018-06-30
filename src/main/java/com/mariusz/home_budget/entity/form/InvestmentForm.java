package com.mariusz.home_budget.entity.form;

import lombok.Data;

import java.time.LocalDate;


@Data
public class InvestmentForm {

    private LocalDate currentDate;
    private String amount;
    private String percentage;
    private int length;
    private String lengthMarker;

}


