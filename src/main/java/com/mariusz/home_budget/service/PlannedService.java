package com.mariusz.home_budget.service;

import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.PlannedOperation;
import com.mariusz.home_budget.entity.form.PlanForm;

import java.util.List;
import java.util.Optional;

public interface PlannedService {

    Optional<String> savePlannedOperation(PlanForm planForm, AppUser user);

    List<PlannedOperation> getPlanedActiveOperation(AppUser user);



    void deletePlan(Long id, AppUser user);

    Optional<PlannedOperation> findByUserAndIdAndActiveIsTrueAndFinishedIsFalse(AppUser user, Long id);

    void savePlannedOperation(PlannedOperation plannedOperation);
}
