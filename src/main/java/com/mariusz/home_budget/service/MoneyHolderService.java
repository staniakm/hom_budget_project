package com.mariusz.home_budget.service;

import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.MoneyHolder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface MoneyHolderService {
    List<MoneyHolder> getMoneyHolders(AppUser user);
    BigDecimal getTotalAmount(AppUser user);
    Optional<MoneyHolder> findByUserAndId(AppUser user,Long holderId);

    void save(MoneyHolder moneyHolder);
}
