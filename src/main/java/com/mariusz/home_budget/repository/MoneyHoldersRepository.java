package com.mariusz.home_budget.repository;

import com.mariusz.home_budget.entity.MoneyHolder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MoneyHoldersRepository extends JpaRepository<MoneyHolder, Long> {

    @Query(value = "select * from money_container m where m.user_id = :user", nativeQuery=true)
    List<MoneyHolder> findAllByUser(@Param("user") Long user);

    @Query(value = "select * from money_container m where m.user_id = :user and m.id = :holder_id", nativeQuery=true)
    Optional<MoneyHolder> findByUserAndId(@Param("user") Long user_id, @Param("holder_id") Long holder_id);
}
