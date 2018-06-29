package com.mariusz.home_budget.helpers;

import com.mariusz.home_budget.entity.AppUser;
import org.springframework.security.core.Authentication;

public interface AuthenticationFacade {
    Authentication getAuthentication();
    String getAuthenticatedUser();
    AppUser getApplicationUser();
}
