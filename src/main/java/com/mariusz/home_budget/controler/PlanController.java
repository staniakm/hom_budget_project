package com.mariusz.home_budget.controler;

import com.mariusz.home_budget.entity.entity_forms.PlanForm;
import com.mariusz.home_budget.entity.entity_forms.WalletForm;
import com.mariusz.home_budget.helpers.AuthenticationFacade;
import com.mariusz.home_budget.helpers.MoneyFlowTypes;
import com.mariusz.home_budget.helpers.MoneyHolderType;
import com.mariusz.home_budget.helpers.PeriodicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

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

        PlanForm planForm = new PlanForm();
        model.addAttribute("planForm", planForm);
        List<PeriodicTypes> operators = Arrays.asList(PeriodicTypes.values());
        model.addAttribute("operators", operators);
        model.addAttribute("currentDate",LocalDate.now());
        model.addAttribute("moneyFlowType",Arrays.asList(MoneyFlowTypes.values()));


        return "plan";
    }


    @GetMapping("/planCashFlow")
    public String planCashFlow(Model model){
        Authentication authentication = authenticationFacade.getAuthentication();
        model.addAttribute(LOGGED_USER, authentication.getName());
        model.addAttribute("fragmentHtml","plan_contents");
        model.addAttribute("fragment","cash_flow");
        model.addAttribute("currentDate", LocalDate.now());

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
