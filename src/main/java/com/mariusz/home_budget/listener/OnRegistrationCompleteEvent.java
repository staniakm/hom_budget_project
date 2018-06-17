package com.mariusz.home_budget.listener;

import com.mariusz.home_budget.entity.AppUser;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

@Data
public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private String appUrl;
    private Locale locale;
    private AppUser user;

    public OnRegistrationCompleteEvent(
            AppUser user, Locale locale, String appUrl) {
        super(user);

        this.user = user;
        this.locale = locale;
        this.appUrl = appUrl;
    }

}