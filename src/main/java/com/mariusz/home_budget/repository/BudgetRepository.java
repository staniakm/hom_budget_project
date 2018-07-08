package com.mariusz.home_budget.repository;

import com.mariusz.home_budget.entity.PlannedBudget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<PlannedBudget, Long> {

    @Query(value = "select * from planned_budget b where b.user_id = :user and year(date)=:year and month(date)=:month", nativeQuery=true)
    List<PlannedBudget> findAllForCurrentMonth(@Param("user") Long user_id
            , @Param("year") int year, @Param("month") int month);

    @Query(value = "select * from planned_budget b where b.user_id = :user and b.category=:category and year(date)=:year and month(date)=:month", nativeQuery=true)
    PlannedBudget getOneByCategory(@Param("user") Long userId, @Param("category") String category,
                                   @Param("year") int year, @Param("month") int month);
}
