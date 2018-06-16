package com.mariusz.home_budget.controler;

import com.mariusz.home_budget.entity.entity_forms.UserForm;
import com.mariusz.home_budget.service.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class MvcApplicationController {

    private final ApplicationUserService applicationUserService;

    @Autowired
    public MvcApplicationController(ApplicationUserService applicationUserService) {
        this.applicationUserService = applicationUserService;
    }


    @GetMapping(value = {"/","/index","/main"})
    public String getMain(){
        return "main";
    }

    @GetMapping("/about")
    public String getAbout(){
        return "about";
    }

    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model){
        UserForm userForm = new UserForm();
        model.addAttribute("userForm", userForm);
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(Model model,@ModelAttribute("userForm") UserForm userForm) {

        Optional<String> success = applicationUserService.registerUser(userForm);

            if (success.isPresent()){
                model.addAttribute("errorMessage", success.get());
                return "register";
            }
            return "redirect:index";
        }
}
