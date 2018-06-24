package com.mariusz.home_budget.repository;

import com.mariusz.home_budget.entity.MoneyHolder;
import com.mariusz.home_budget.entity.PlannedOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlannedRepository extends JpaRepository<PlannedOperation, Long> {

    @Query(value = "select * from planned_operation m where m.user_id = :user " +
            "and m.is_active = 1 and m.is_finished = 0 order by m.due_date asc", nativeQuery=true)
    List<PlannedOperation> getPlanedActivitiesOperations(@Param("user") Long id);

}
