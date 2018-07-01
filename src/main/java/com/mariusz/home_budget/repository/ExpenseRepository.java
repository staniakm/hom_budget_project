package com.mariusz.home_budget.repository;

import com.mariusz.home_budget.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    @Query(value = "select * from expense where user_id = :user and id=:id", nativeQuery=true)
    Expense getExpenseByIdAndUser(@Param("id") Long operationId, @Param("user") Long userId);
}
