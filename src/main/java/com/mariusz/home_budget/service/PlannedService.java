package com.mariusz.home_budget.service;

import com.mariusz.home_budget.entity.entity_forms.PlanForm;

import java.util.Optional;

public interface PlannedService {

    Optional<String> savePlannedOperation(PlanForm planForm, String name);
}
