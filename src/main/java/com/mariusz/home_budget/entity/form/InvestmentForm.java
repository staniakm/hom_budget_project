package com.mariusz.home_budget.entity.form;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mariusz.home_budget.entity.MoneyHolder;
import com.mariusz.home_budget.helpers.LengthKeeper;
import com.mariusz.home_budget.validators.Amount;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@Data
@ToString
public class InvestmentForm {

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @Amount
    private String amount;
    @NotNull
    @Amount
    private String percentage;
    @NotNull
    private int length;
    private LengthKeeper investmentLength;

    @JsonIgnore
    @OneToOne
    private MoneyHolder moneyHolder;

    public void setAmount(String amount) {
        this.amount =amount.replace(",",".");
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage.replace(",",".");
    }

    public void setDate(LocalDate date){
        this.date =  date;
    }
}


