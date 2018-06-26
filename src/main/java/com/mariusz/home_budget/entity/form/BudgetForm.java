package com.mariusz.home_budget.entity.form;

import com.mariusz.home_budget.validators.Amount;
import lombok.Data;

@Data
public class BudgetForm {

    private String description;
    @Amount
    private String amount;
    private String category;

    public void setAmount(String amount) {
        this.amount =amount.replace(",",".");
    }


}
