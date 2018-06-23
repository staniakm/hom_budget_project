package com.mariusz.home_budget.controler;

import com.mariusz.home_budget.helpers.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PlanController {
    private static final String LOGGED_USER = "loggedUser";

    private final AuthenticationFacade authenticationFacade;

    @Autowired
    public PlanController(AuthenticationFacade authenticationFacade) {
        this.authenticationFacade = authenticationFacade;
    }

    @GetMapping("/plan")
    public String getPlanPage (Model model){
        Authentication authentication = authenticationFacade.getAuthentication();
        model.addAttribute(LOGGED_USER, authentication.getName());
        model.addAttribute("fragmentHtml","plan_contents");
        model.addAttribute("fragment","empty_content");
        return "plan";
    }

    @GetMapping("/planExpenses")
    public String planExpenses(Model model){
        model.addAttribute("fragmentHtml","plan_contents");
        model.addAttribute("fragment","planner");
        Authentication authentication = authenticationFacade.getAuthentication();
        model.addAttribute(LOGGED_USER, authentication.getName());

        return "plan";
    }


    @GetMapping("/planCashFlow")
    public String planCashFlow(Model model){
        Authentication authentication = authenticationFacade.getAuthentication();
        model.addAttribute(LOGGED_USER, authentication.getName());
        model.addAttribute("fragmentHtml","plan_contents");
        model.addAttribute("fragment","cash_flow");

        return "plan";
    }


    @GetMapping("/planBudget")
    public String planBudget(Model model){
        model.addAttribute("fragmentHtml","plan_contents");
        model.addAttribute("fragment","budget");
        Authentication authentication = authenticationFacade.getAuthentication();
        model.addAttribute("loggedUser", authentication.getName());
        return "plan";
    }
}
