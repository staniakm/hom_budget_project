package com.mariusz.home_budget.service;

import com.mariusz.home_budget.entity.ShopingList;
import com.mariusz.home_budget.entity.ShoppingItem;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShoppingServiceImpl implements ShoppingService {


    @Override
    public ShopingList getLastShoppingList() {
        ShopingList shopingList = new ShopingList();
        shopingList.setCreationTime(LocalDateTime.now());
        shopingList.setId(1L);
        List<ShoppingItem> items = new ArrayList<>();
        items.add(new ShoppingItem(1L, "Mleko",BigDecimal.valueOf(1.89)));
        items.add(new ShoppingItem(2L, "Maslo",BigDecimal.valueOf(4.89)));
        items.add(new ShoppingItem(3L, "Chleb",BigDecimal.valueOf(1.59)));
        items.add(new ShoppingItem(4L, "Wedlina",BigDecimal.valueOf(2.50)));
        shopingList.setItems(items);
        BigDecimal result = items.stream().map(ShoppingItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        shopingList.setTotalAmount(result);
        return shopingList;

    }
}
