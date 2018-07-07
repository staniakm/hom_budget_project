package com.mariusz.home_budget.repository;

import com.mariusz.home_budget.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {

    @Query(value = "select * from income  where user_id = :user and id=:id", nativeQuery=true)
    Income getIncomeByIdAndUser(@Param("id") Long operationId,@Param("user") Long user_id);

}
