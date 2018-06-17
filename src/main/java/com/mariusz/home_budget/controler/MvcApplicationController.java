package com.mariusz.home_budget.controler;

import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.entity_forms.UserForm;
import com.mariusz.home_budget.listener.OnRegistrationCompleteEvent;
import com.mariusz.home_budget.service.ApplicationUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Locale;
import java.util.Optional;

@Controller
public class MvcApplicationController {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Qualifier("messageSource")
    @Autowired
    private MessageSource messages;

    private final ApplicationUserService applicationUserService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public MvcApplicationController(ApplicationUserService applicationUserService, ApplicationEventPublisher applicationEventPublisher) {
        this.applicationUserService = applicationUserService;
        this.applicationEventPublisher = applicationEventPublisher;
    }


    @GetMapping(value = {"/","/index","/main"})
    public String getMain(){
        return "main";
    }

    @GetMapping("/about")
    public String getAbout(){
        return "about";
    }

    //user enter login page.
    @GetMapping("/login")
    public String getLoginPage(){
        return "login";
    }

    //Verification process done by Spring security.
    @GetMapping("/welcome")
    public String loginProccess(Model model, Authentication authentication){
        return "welcome";
    }

    //page load while enters register page.
    @GetMapping("/register")
    public String registerPage(Model model){

        UserForm userForm = new UserForm();
        model.addAttribute("userForm", userForm);
        return "register";
    }

    //user try to register new account. Validation and registration.
    @PostMapping("/register")
    public ModelAndView registerUser(Model model , @ModelAttribute("userForm") UserForm userAccount
                , BindingResult result, HttpServletRequest request) {

        Optional<String> errorOccur = applicationUserService.registerUser(userAccount);

        if (errorOccur.isPresent()){
            model.addAttribute("errorMessage", errorOccur.get());
            return new ModelAndView("register", "userForm", userAccount);
        }

        Optional<AppUser> user = applicationUserService.getUserByName(userAccount.getName());
        if (!user.isPresent()) {
            result.rejectValue("email", "message.regError");
        }else {
            try {
                String appUrl = getAppUrl(request);
                applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent
                        (user.get(), request.getLocale(), appUrl));
            } catch (Exception me) {
                return new ModelAndView("emailError", "userForm", userAccount);
            }
        }

        return new ModelAndView("login", "userForm", userAccount);
    }

    @RequestMapping(value = "/registrationConfirm", method = RequestMethod.GET)
    public String confirmRegistration(final HttpServletRequest request, final Model model, @RequestParam("token") final String token) throws UnsupportedEncodingException {
        Locale locale = request.getLocale();
        final String result = applicationUserService.validateVerificationToken(token);
        if (result.equals("valid")) {
            final AppUser user = applicationUserService.getUserByToken(token);
            // if (user.isUsing2FA()) {
            // model.addAttribute("qr", userService.generateQRUrl(user));
            // return "redirect:/qrcode.html?lang=" + locale.getLanguage();
            // }
            authWithoutPassword(user);
            model.addAttribute("message", messages.getMessage("message.accountVerified", null, locale));
            return "redirect:/welcome";
        }

        model.addAttribute("message", result);
        model.addAttribute("expired", "expired".equals(result));
        model.addAttribute("token", token);
        return "badUser";
    }


    private String getAppUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    public void authWithoutPassword(AppUser user) {

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, null);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
