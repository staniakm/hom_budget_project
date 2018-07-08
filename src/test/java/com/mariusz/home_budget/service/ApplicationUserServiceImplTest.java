package com.mariusz.home_budget.service;

import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.VerificationToken;
import com.mariusz.home_budget.entity.form.UserForm;
import com.mariusz.home_budget.repository.UserRepository;
import com.mariusz.home_budget.repository.VerificationTokenRepository;
import org.apache.commons.validator.routines.EmailValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class ApplicationUserServiceImplTest {



    @Configuration
    static class EmployeeServiceImplContextConfiguration{
        private UserRepository userRepository = mock(UserRepository.class);
        private VerificationTokenRepository verificationTokenRepository = mock(VerificationTokenRepository.class);
        private EmailValidator emailValidator = EmailValidator.getInstance();
        @Bean
        public ApplicationUserService applicationUserService(){
            PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

            return new ApplicationUserServiceImpl(passwordEncoder,userRepository,emailValidator,verificationTokenRepository);
        }

        @Bean
        public UserRepository userRepository(){
            return userRepository;
        }

        @Bean VerificationTokenRepository verificationTokenRepository(){
            return verificationTokenRepository;
        }

    }

    @Autowired
    private ApplicationUserService applicationUserService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Before
    public void setup(){
        AppUser user = new AppUser();
        user.setEnabled(true);
        user.setName("Mariusz");
        user.setPassword("secret");
        user.setId(10L);

        AppUser properUser = new AppUser();
        properUser.setEnabled(true);
        properUser.setName("takenUser@email.pl");
        properUser.setPassword("secret");
        properUser.setId(20L);

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken("valid token");
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusMinutes(5));

        VerificationToken expiredVerificationToken = new VerificationToken();
        expiredVerificationToken.setToken("expired token");
        expiredVerificationToken.setUser(user);
        expiredVerificationToken.setExpiryDate(LocalDateTime.now().minusMinutes(15));


        when(userRepository.findByName(user.getName())).thenReturn(user);

        when(userRepository.findByName(properUser.getName())).thenReturn(properUser);

        when(userRepository.findByName("properUser@email.pl")).thenReturn(null);

        when(verificationTokenRepository.findByToken(verificationToken.getToken())).thenReturn(verificationToken);

        when(verificationTokenRepository.findByToken(expiredVerificationToken.getToken())).thenReturn(expiredVerificationToken);

    }

    @Test
    public void whenValidName_AppUserShouldBeReturned(){
        String name = "Mariusz";
        AppUser found = applicationUserService.getUserByName(name);
        assertThat(found.getName()).isEqualTo(name);
        assertThat(found.getPassword()).isEqualTo("secret");
    }

    @Test(expected = UsernameNotFoundException.class)
    public void whenInvalidName_ExceptionShouldBeThrown(){
        String name = "Asia";
        AppUser found = applicationUserService.getUserByName(name);
        assertThat(found.getName()).isEqualTo(name);
    }



    @Test
    public void incorrectEmailShouldReturnNonEmptyOptionalForIncorrectEmail(){
        UserForm userForm = new UserForm();
        userForm.setName("Mariusz");
        userForm.setPassword("secret");
        userForm.setConfirmedPassword("secret");

        String error= "";
        Optional<String> result = applicationUserService.registerUser(userForm);
        if (result.isPresent())
            error = result.get();
        assertThat(error).isEqualTo("Email address must be valid.");
    }

    @Test
    public void nullableEmailShouldReturnNonEmptyOptionalForIncorrectEmail(){
        UserForm userForm = new UserForm();
        userForm.setName(null);
        userForm.setPassword("secret");
        userForm.setConfirmedPassword("secret");

        String error= "";
        Optional<String> result = applicationUserService.registerUser(userForm);
        if (result.isPresent())
            error = result.get();
        assertThat(error).isEqualTo("Email address cannot be empty.");
    }

    @Test
    public void spacesWillBeTrimedInEmail_ShouldReturnNonEmptyOptionalForIncorrectEmail(){
        UserForm userForm = new UserForm();
        userForm.setName("   ");
        userForm.setPassword("secret");
        userForm.setConfirmedPassword("secret");

        String error= "";
        Optional<String> result = applicationUserService.registerUser(userForm);
        if (result.isPresent())
            error = result.get();
        assertThat(error).isEqualTo("Email address cannot be empty.");
    }

    @Test
    public void emptyPassword_returnNonEmptyOptionalForPasswordIncorrest(){
        UserForm userForm = new UserForm();
        userForm.setName("test@test.pl");
        userForm.setPassword("");
        userForm.setConfirmedPassword("");

        String error= "";
        Optional<String> result = applicationUserService.registerUser(userForm);
        if (result.isPresent())
            error = result.get();
        assertThat(error).isEqualTo("Password should be at least 6 chars long.");
    }

    @Test
    public void passwordShorterThen_returnNonEmptyOptionalForPasswordIncorrest(){
        UserForm userForm = new UserForm();
        userForm.setName("test@test.pl");
        userForm.setPassword("secre");
        userForm.setConfirmedPassword("secre");

        String error= "";
        Optional<String> result = applicationUserService.registerUser(userForm);
        if (result.isPresent())
            error = result.get();
        assertThat(error).isEqualTo("Password should be at least 6 chars long.");
    }


    @Test
    public void passwordLongerOrEqualToSix_returnEmptyOptional(){
        UserForm userForm = new UserForm();
        userForm.setName("test@test.pl");
        userForm.setPassword("secret");
        userForm.setConfirmedPassword("secret");

        Optional<String> result = applicationUserService.registerUser(userForm);
        assertThat(result).isEqualTo(Optional.empty());
    }


    @Test
    public void passwordConfirmationMustBeEqualToPassword(){
        UserForm userForm = new UserForm();
        userForm.setName("test@test.pl");
        userForm.setPassword("secret");
        userForm.setConfirmedPassword("secrets");

        String error= "";
        Optional<String> result = applicationUserService.registerUser(userForm);
        if (result.isPresent())
            error = result.get();
        assertThat(error).isEqualTo("Confirmation password must be these same as password.");
    }

    @Test
    public void takenUsernameReturnNonEmptyOptional(){
        UserForm userForm = new UserForm();
        userForm.setName("takenUser@email.pl");
        userForm.setPassword("secret");
        userForm.setConfirmedPassword("secret");

        String error= "";
        Optional<String> result = applicationUserService.registerUser(userForm);
        if (result.isPresent())
            error = result.get();
        assertThat(error).isEqualTo("User name already taken.");
    }

    @Test
    public void shouldReturnEnptyOptionalIfRegistrationSuceed(){
        UserForm userForm = new UserForm();
        userForm.setName("properUser@email.pl");
        userForm.setPassword("secretPassword");
        userForm.setConfirmedPassword("secretPassword");

        Optional<String> result = applicationUserService.registerUser(userForm);
        verify(userRepository,times(1)).findByName(userForm.getName());

        assertThat(result).isEqualTo(Optional.empty());
    }


    @Test
    public void tokenIsValid_shouldReturnUser() {
        String token = "valid token";
        String userName = "Mariusz";
        String isValid = applicationUserService.validateVerificationToken(token);
        assertThat(isValid).isEqualTo("valid");
        AppUser user = applicationUserService.getUserByToken(token);
        assertThat(user.getName()).isEqualTo(userName);
    }


    @Test
    public void tokenIsExpired() {
        String token = "expired token";
        String isValid = applicationUserService.validateVerificationToken(token);
        assertThat(isValid).isEqualTo("expired");
    }

    @Test
    public void tokenIsInvalid() {
        String token = "invalid token";
        String isValid = applicationUserService.validateVerificationToken(token);
        assertThat(isValid).isEqualTo("invalidToken");
    }
}