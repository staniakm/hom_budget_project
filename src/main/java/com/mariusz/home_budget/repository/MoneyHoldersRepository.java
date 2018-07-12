package com.mariusz.home_budget.repository;

import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.MoneyHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MoneyHoldersRepository extends JpaRepository<MoneyHolder, Long> {

    List<MoneyHolder> findAllByUser( AppUser user);
    Optional<MoneyHolder> findByUserAndId(AppUser user, Long holder_id);

}
