package com.mariusz.home_budget.entity.entity_forms;

import com.mariusz.home_budget.helpers.MoneyHolderType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WalletForm {

    private String name;
    private String cash;
    private String user;
    private String moneyHolderType;
}
