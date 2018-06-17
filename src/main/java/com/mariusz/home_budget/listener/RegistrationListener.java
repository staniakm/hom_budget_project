package com.mariusz.home_budget.listener;

import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.service.ApplicationUserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationListener implements
        ApplicationListener<OnRegistrationCompleteEvent> {

    private final ApplicationUserService service;

    private final MessageSource messages;

    private final JavaMailSender mailSender;

    private final Environment env;

    public RegistrationListener(ApplicationUserService service, @Qualifier("messageSource") MessageSource messages, JavaMailSender mailSender, Environment env) {
        this.service = service;
        this.messages = messages;
        this.mailSender = mailSender;
        this.env = env;
    }


    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        final AppUser user = event.getUser();
        final String token = UUID.randomUUID().toString();
        service.createVerificationToken(user, token);

        final SimpleMailMessage email = constructEmailMessage(event, user, token);
        mailSender.send(email);
    }

    private SimpleMailMessage constructEmailMessage(final OnRegistrationCompleteEvent event, final AppUser user, final String token) {
        final String recipientAddress = user.getName();
        final String subject = "Registration Confirmation";
        final String confirmationUrl = event.getAppUrl() + "/registrationConfirm?token=" + token;
        final String message = messages.getMessage("message.regSucc", null, event.getLocale());
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + " \r\n" + confirmationUrl);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }
}