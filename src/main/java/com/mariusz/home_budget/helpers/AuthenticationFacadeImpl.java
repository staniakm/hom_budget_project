package com.mariusz.home_budget.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacadeImpl implements AuthenticationFacade {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Authentication getAuthentication() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logger.info("AUTH info: " +auth.toString());
        return auth;
    }
}
