package com.mariusz.home_budget.service;

import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.entity_forms.UserForm;
import com.mariusz.home_budget.repository.UserRepository;
import com.mariusz.home_budget.repository.VerificationTokenRepository;
import com.mariusz.home_budget.entity.VerificationToken;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@Service
public class ApplicationUserServiceImpl implements ApplicationUserService, UserDetailsService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String TOKEN_INVALID = "invalidToken";
    private static final String TOKEN_EXPIRED = "expired";
    private static final String TOKEN_VALID = "valid";

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final EmailValidator emailValidator;
    private final VerificationTokenRepository verificationTokenRepository;

    @Autowired
    public ApplicationUserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, EmailValidator emailValidator, VerificationTokenRepository verificationTokenRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.emailValidator = emailValidator;
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        AppUser appUser = userRepository.findByName(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        return toUser(appUser);
    }

    private UserDetails toUser(AppUser appUser) {
        return User.builder()
                .username(appUser.getName())
                .password(appUser.getPassword())
                .disabled(!appUser.isEnabled())
                .authorities(Collections.emptyList())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .build();
    }

    @SuppressWarnings("ConstantConditions")
    @Transactional
    public Optional<String> registerUser(UserForm userForm) {

        logger.info("User registration process");
        String name = userForm.getName().trim();
        String pass = userForm.getPassword().trim();
        String passConfirmation = userForm.getConfirmedPassword().trim();

        if (name==null || name.length()==0 || !emailValidator.isValid(name)){
            return Optional.of("Email address must be valid.");
        }

        if (pass==null || pass.length()<6){
            return Optional.of("Password should be at least 6 chars long.");
        }else if (!pass.equals(passConfirmation)){
                return Optional.of("Confirmation password is not theses same as password.");
        }

        if (userRepository.findByName(name).isPresent()){
            return Optional.of("User name already taken.");
        }

        AppUser appUser = new AppUser();
        appUser.setName(userForm.getName());
        appUser.setPassword(passwordEncoder.encode(userForm.getPassword()));
        userRepository.save(appUser);

        return Optional.empty();
    }

    @Override
    public Optional<AppUser> getUserByName(String name) {
       return userRepository.findByName(name);

    }

    @Override
    public AppUser getUserByToken(String verificationToken) {
        return verificationTokenRepository.findByToken(verificationToken).getUser();
    }

    @Override
    public void createVerificationToken(AppUser user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        verificationTokenRepository.save(myToken);
    }


    @Override
    public String validateVerificationToken(String token) {
        final VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            return TOKEN_INVALID;
        }

        final AppUser user = verificationToken.getUser();
        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            verificationTokenRepository.delete(verificationToken);
            return TOKEN_EXPIRED;
        }

        user.setEnabled(true);
        userRepository.save(user);
        return TOKEN_VALID;
    }

}
