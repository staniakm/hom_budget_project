package com.mariusz.home_budget.repository;

import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

    Income findByUserAndId(AppUser user, Long incomeId);

}
