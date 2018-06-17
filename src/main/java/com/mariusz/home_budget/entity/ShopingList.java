package com.mariusz.home_budget.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopingList {

    private Long id;
    private LocalDateTime creationTime;
    private BigDecimal totalAmount;

    private List<ShoppingItem> items;


}
