package com.mariusz.home_budget.service;

import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.MoneyHolder;
import com.mariusz.home_budget.repository.MoneyHoldersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class MoneyHolderService {

    @Autowired
    private MoneyHoldersRepository moneyHoldersRepository;

    /**
     * Get list of all money holders for current logged user.
     *
     * @param user - current logged user
     * @return - list of money holders
     */

    public List<MoneyHolder> getMoneyHolders(AppUser user) {
        return moneyHoldersRepository.findAllByUser(user);

    }


    public Optional<MoneyHolder> findByUserAndId(AppUser user, Long holderId) {
        return Optional.empty();
    }

    /**
     * Get total amount of money from all wallets
     *
     * @param user (current logged user)
     * @return - BigDecimal as sum of money in all user wallets
     */

    public BigDecimal getTotalAmount(AppUser user) {
        BigDecimal amount = moneyHoldersRepository.findAllByUser(user).stream().map(MoneyHolder::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        return amount == null ? BigDecimal.ZERO : amount;
    }


    public void save(MoneyHolder moneyHolder) {
        moneyHoldersRepository.save(moneyHolder);
    }
}
