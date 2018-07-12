package com.mariusz.home_budget.repository;

import com.mariusz.home_budget.entity.Balance;
import com.mariusz.home_budget.entity.MoneyFlowSimple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("ALL")
@Repository
public class FinancialCustomRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FinancialCustomRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //TODO move complex calculations to database store procedures

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
        String sql = "select id, user_id, date, description, amount, operation, category from (\n" +
                "SELECT id, user_id, date, description, amount, 'income' as operation, ifnull(category,'-') as category FROM `income`\n" +
                "union \n" +
                "select id, user_id, date, description, amount, 'expense' as operation, ifnull(category,'-') as category from expense\n" +
                ") as x\n" +
                "where x.user_id=? and year(x.date) = ? and month(x.date) = ? order by x.date";
        List<MoneyFlowSimple> moneyFlowSimples = jdbcTemplate.query(sql,
                    new BeanPropertyRowMapper<>(MoneyFlowSimple.class),user_id, year,month);
        return moneyFlowSimples;
    }

    public void recalculateBudgets(@Param("user_id")Long user_id, @Param("year") int year, @Param("month") int month){
        String sql = "update planned_budget b\n" +
                "set b.spend = ifnull((select  COALESCE(sum(e.amount),0) from expense e " +
                "where b.user_id = e.user_id and e.category = b.category and year(b.date) = year(e.date) and month(b.date)=month(e.date) group by e.category, e.user_id),0)\n" +
                "where b.user_id = ? and year(b.date) = ? and month(b.date)=?";
        jdbcTemplate.update(sql,user_id,year,month);
    }

    @Modifying
    public void clearTokens() {
        String sql = "delete from verification_tokens where expiry_date < ?";
        jdbcTemplate.update(sql, LocalDateTime.now());
    }
}
