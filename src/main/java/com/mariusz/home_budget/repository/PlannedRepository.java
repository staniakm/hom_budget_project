package com.mariusz.home_budget.repository;

import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.PlannedOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlannedRepository extends JpaRepository<PlannedOperation, Long> {

    List<PlannedOperation> findByUserAndActiveTrueAndFinishedFalseOrderByDueDateAsc(AppUser user);
    Optional<PlannedOperation> findByUserAndIdAndActiveIsTrueAndFinishedIsFalse(AppUser user, Long operationId);

}
