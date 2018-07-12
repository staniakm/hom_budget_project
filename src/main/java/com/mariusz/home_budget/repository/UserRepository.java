package com.mariusz.home_budget.repository;

import com.mariusz.home_budget.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByName(String name);
}
