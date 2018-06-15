package com.mariusz.home_budget.service;

import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.entity_forms.UserForm;
import com.mariusz.home_budget.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class ApplicationUserServiceImpl implements ApplicationUserService, UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public ApplicationUserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }


//    @Override
//    public boolean checkUserByUsername(String name){
//        Optional<AppUser> appUser = userRepository.findByName(name);
//        return appUser.isPresent();
//    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findByName(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));


        return toUser(appUser);
    }

    private UserDetails toUser(AppUser appUser) {
        return User.builder()
                .username(appUser.getName())
                .password(appUser.getPassword())
                .authorities(Collections.EMPTY_LIST)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .build();
    }

    public void saveUser(UserForm userForm) {

        AppUser appUser = new AppUser();
        appUser.setName(userForm.getName());
        appUser.setPassword(passwordEncoder.encode(userForm.getPassword()));
        userRepository.save(appUser);
    }


}
