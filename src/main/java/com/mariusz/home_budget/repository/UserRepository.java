package com.mariusz.home_budget.repository;

import com.mariusz.home_budget.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByName(String name);


}
