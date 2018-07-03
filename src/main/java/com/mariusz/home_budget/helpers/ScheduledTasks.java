package com.mariusz.home_budget.helpers;

import com.mariusz.home_budget.service.FinancialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ScheduledTasks {

    @Autowired
    private FinancialService financialService;

    @Scheduled(cron = "0 0 1 * * ?")
    public void scheduleTaskWithFixedRate() {
        financialService.clearTokens();
}

}
