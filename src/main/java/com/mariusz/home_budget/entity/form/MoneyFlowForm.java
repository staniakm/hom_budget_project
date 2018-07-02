package com.mariusz.home_budget.entity.form;

import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.validators.Amount;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@ToString
public class MoneyFlowForm {

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @NotNull
    private String description;

    @Amount
    private String amount;
    private String operation;
    private AppUser user;
    private String moneyHolder;
    private String category;

    public void setDescription(String description) {
        this.description = description.trim();
    }

    public void setAmount(String amount) {
        this.amount =amount.replace(",",".");
    }

}
