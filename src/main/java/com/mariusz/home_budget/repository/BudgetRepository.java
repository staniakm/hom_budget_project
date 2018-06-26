package com.mariusz.home_budget.repository;

import com.mariusz.home_budget.entity.Budget;
import com.mariusz.home_budget.entity.PlannedBudget;
import org.apache.tomcat.jni.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<PlannedBudget, Long> {

    @Query(value = "select * from planned_budget b where b.user_id = :user and year(date)=:year and month(date)=:month", nativeQuery=true)
    List<PlannedBudget> findAllByUser(@Param("user") Long id
            , @Param("year") int year, @Param("month") int month);

}
