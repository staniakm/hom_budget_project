package com.mariusz.home_budget.mapper;

import com.mariusz.home_budget.entity.Balance;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BalanceRowMapper implements RowMapper<Balance> {
    @Override
    public Balance mapRow(ResultSet resultSet, int i) throws SQLException {
        Balance balance = new Balance();
        balance.setId(1);
        balance.setIncome(resultSet.getBigDecimal("income"));
        balance.setExpense(resultSet.getBigDecimal("expense"));

        return balance;

    }
}
