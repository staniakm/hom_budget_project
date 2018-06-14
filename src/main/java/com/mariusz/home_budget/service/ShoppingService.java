package com.mariusz.home_budget.service;

import com.mariusz.home_budget.entity.ShopingList;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

public interface ShoppingService  {


    ShopingList getLastShoppingList();
}
