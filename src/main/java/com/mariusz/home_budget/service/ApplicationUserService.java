package com.mariusz.home_budget.service;

import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.form.UserForm;

import java.util.Optional;

public interface ApplicationUserService {

    Optional<String> registerUser(UserForm userForm);

    Optional<AppUser> getUserByName(String name);

    AppUser getUserByToken(String verificationToken);

    void createVerificationToken(AppUser user, String token);

    String validateVerificationToken(String token);
}
