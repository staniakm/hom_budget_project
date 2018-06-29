package com.mariusz.home_budget.entity;

import com.mariusz.home_budget.helpers.MonthTranslator;
import com.mariusz.home_budget.service.MessagesService;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MonthKeeper {

    private String monthName;
    private Integer previous;
    private Integer next;
    private Integer current;

    public MonthKeeper(Integer month, MessagesService messagesService) {

        if (month==null){
            current=LocalDate.now().getMonthValue();
        }else{
            current = month;
        }

        MonthTranslator monthTranslator = MonthTranslator.JANUARY;
        for (MonthTranslator m: MonthTranslator.values()
                ) {
            if (m.getMonthNumber()==current){
                monthTranslator =m;
                break;
            }
        }

        this.setMonthName(messagesService.getMessage(monthTranslator.getMonth()).toUpperCase());
        if(current==1){
            previous=12;
            next=2;
        }else if (current==12){
            next=1;
            previous=11;
        }else{
            this.next=monthTranslator.getMonthNumber()+1;
            this.previous=monthTranslator.getMonthNumber()-1;
        }


    }

}
