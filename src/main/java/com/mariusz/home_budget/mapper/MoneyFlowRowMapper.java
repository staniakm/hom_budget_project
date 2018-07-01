package com.mariusz.home_budget.mapper;

import com.mariusz.home_budget.entity.MoneyFlowSimple;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MoneyFlowRowMapper implements RowMapper<MoneyFlowSimple> {
    @Override
    public MoneyFlowSimple mapRow(ResultSet resultSet, int i) throws SQLException {
        MoneyFlowSimple moneyFlowSimple = new MoneyFlowSimple();
        moneyFlowSimple.setAmount(resultSet.getBigDecimal("amount"));
        moneyFlowSimple.setDate(resultSet.getDate("date").toLocalDate());
        moneyFlowSimple.setDescription(resultSet.getString("description"));
        moneyFlowSimple.setOperation(resultSet.getString("operation"));

        return moneyFlowSimple;

    }
}
