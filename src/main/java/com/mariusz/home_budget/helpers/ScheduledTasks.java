package com.mariusz.home_budget.helpers;

import com.mariusz.home_budget.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ScheduledTasks {

    @Autowired
    private ScheduleService scheduleService;

    //cron expressions
    //https://stackoverflow.com/questions/26147044/spring-cron-expression-for-every-day-101am
    //* "0 0 * * * *" = the top of every hour of every day.
    //* "*/10 * * * * *" = every ten seconds.
    //* "0 0 8-10 * * *" = 8, 9 and 10 o'clock of every day.
    //* "0 0 8,10 * * *" = 8 and 10 o'clock of every day.
    //* "0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30 and 10 o'clock every day.
    //* "0 0 9-17 * * MON-FRI" = on the hour nine-to-five weekdays
    //* "0 0 0 25 12 ?" = every Christmas Day at midnight
    //second, minute, hour, day of month, month, day(s) of week

    /**
     * scheduler responsible for clearing old tokens every night as 1:00
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void deleteOldTokens() {
        scheduleService.clearTokens();
    }

    /**
     * scheduler responsible for clearing inactive accounts every night as 1:30
     */
    @Scheduled(cron = "0 30 1 * * ?")
    public void deleteInactiveAccounts(){
        scheduleService.clearInactiveAccounts();
    }

    /**
     * scheduler responsible for update currency rate every night as 5:00
     */
    @Scheduled(cron = "0 0 5 * * ?")
    public void updateCurrencyRate(){
        System.out.println("Currency update task");
        scheduleService.updateCurrencyRate();
    }


}
