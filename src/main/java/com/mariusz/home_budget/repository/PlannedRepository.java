package com.mariusz.home_budget.repository;

import com.mariusz.home_budget.entity.PlannedOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlannedRepository extends JpaRepository<PlannedOperation, Long> {


}
