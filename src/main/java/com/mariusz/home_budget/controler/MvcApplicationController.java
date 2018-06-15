package com.mariusz.home_budget.controler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MvcApplicationController {

    @GetMapping(value = {"/","/index"})
    public String getMain(){
        return "main";
    }

    @GetMapping("/about")
    public String getAbout(){
        return "about";
    }

    @GetMapping("/login")
    public String loginpage(){
        return "login";
    }

}
