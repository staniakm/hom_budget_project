package com.mariusz.home_budget.entity.form;

import com.mariusz.home_budget.helpers.LengthKeeper;
import lombok.Data;

import java.time.LocalDate;


@Data
public class InvestmentForm {

    private LocalDate startDate;
    private String amount;
    private String percentage;
    private int length;
    private LengthKeeper investmentLength;

}


