package com.mariusz.home_budget.helpers;

import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.service.ApplicationUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacadeImpl implements AuthenticationFacade {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ApplicationUserService userService;

    public AuthenticationFacadeImpl(ApplicationUserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public String getAuthenticatedUser() {
        return getAuthentication().getName();
    }

    @Override
    public AppUser getApplicationUser() {
        return userService.getUserByName(getAuthenticatedUser()).orElseThrow(()->new UsernameNotFoundException(""));

    }


}
