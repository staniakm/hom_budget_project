package com.mariusz.home_budget.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "money_container")
@Data
public class Wallet extends MoneyHolder {

    public Wallet() {
    }

}
