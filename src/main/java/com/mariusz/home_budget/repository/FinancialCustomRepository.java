package com.mariusz.home_budget.repository;

import com.mariusz.home_budget.entity.Balance;
import com.mariusz.home_budget.entity.MoneyFlowSimple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("ALL")
@Repository
public class FinancialCustomRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FinancialCustomRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @SuppressWarnings("SqlResolve")
    public Balance getBalance(@Param("user_id")Long user_id, @Param("year") int year, @Param("month") int month){
        String sql = "select ifnull((select sum(amount) as suma from income where user_id=? and year(date) = ? " +
                "and month(date) = ?),0) as 'income',ifnull((\n" +
                "select sum(amount) from expense where user_id=? and year(date) = ? " +
                "and month(date) = ?),0) as 'expense'\n";
        RowMapper<Balance> rowMapper = new BeanPropertyRowMapper<Balance>(Balance.class);
        return jdbcTemplate.queryForObject(sql,rowMapper,user_id,year,month,user_id,year,month);
    }

    public List<MoneyFlowSimple> getMoneyFlow(@Param("user_id")Long user_id, @Param("year") int year, @Param("month") int month){
        String sql = "select user_id, date, description, amount, operation from (\n" +
                "SELECT user_id, date, description, amount, 'income' as operation FROM `income`\n" +
                "union \n" +
                "select user_id, date, description, amount, 'expense' as operation from expense\n" +
                ") as x\n" +
                "where x.user_id=? and year(x.date) = ? and month(x.date) = ? order by x.date";
        List<MoneyFlowSimple> moneyFlowSimples = jdbcTemplate.query(sql,
                    new BeanPropertyRowMapper<>(MoneyFlowSimple.class),user_id, year,month);
        return moneyFlowSimples;
    }

    public void recalculateBudgets(@Param("user_id")Long user_id, @Param("year") int year, @Param("month") int month){
        String sql = "update planned_budget b\n" +
                "set b.spend = ifnull((select  COALESCE(sum(e.amount),0) from expense e where e.category = b.category and year(b.date) = year(e.date) and month(b.date)=month(e.date) group by e.category, e.user_id),0)\n" +
                "where b.user_id = ? and year(b.date) = ? and month(b.date)=?";
        jdbcTemplate.update(sql,user_id,year,month);
    }
}
