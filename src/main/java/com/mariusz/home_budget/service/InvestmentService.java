package com.mariusz.home_budget.service;

import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.Investment;
import com.mariusz.home_budget.entity.form.InvestmentForm;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface InvestmentService {

    Optional<String> addInvestment(InvestmentForm investmentForm, AppUser user);
    List<Investment> getInvestments(AppUser user);
    Investment getInvestmentsById(AppUser user, Long id);
    BigDecimal getInvestmentsSum(AppUser user);



}
