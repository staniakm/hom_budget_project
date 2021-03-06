package com.mariusz.home_budget.repository;

import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.Investment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Long> {

    List<Investment> findAllByUserAndActiveTrue(AppUser user);
    Investment findByUserAndId(AppUser user, Long investmentId);

}
