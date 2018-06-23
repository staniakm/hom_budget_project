package com.mariusz.home_budget.controler;

import com.mariusz.home_budget.entity.entity_forms.PlanForm;
import com.mariusz.home_budget.helpers.AuthenticationFacade;
import com.mariusz.home_budget.helpers.MoneyFlowTypes;
import com.mariusz.home_budget.helpers.PeriodicTypes;
import com.mariusz.home_budget.service.PlannedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class PlanController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String LOGGED_USER = "loggedUser";
    private static final String FRAGMENT_HTML_FILE = "fragmentHtml";
    private static final String FRAGMENT_HTML_REPLACE_NAME = "fragment";


    private final PlannedService plannedService;
    private final AuthenticationFacade authenticationFacade;

    @Autowired
    public PlanController(PlannedService plannedService, AuthenticationFacade authenticationFacade) {
        this.plannedService = plannedService;
        this.authenticationFacade = authenticationFacade;
    }

    @GetMapping("/plan")
    public String getPlanPage (Model model){
        Authentication authentication = authenticationFacade.getAuthentication();
        model.addAttribute(LOGGED_USER, authentication.getName());
        model.addAttribute(FRAGMENT_HTML_FILE,"plan_contents");
        model.addAttribute(FRAGMENT_HTML_REPLACE_NAME,"empty_content");
        return "plan";
    }

    @GetMapping("/planExpenses")
    public String planExpenses(Model model){
        model.addAttribute(FRAGMENT_HTML_FILE,"plan_contents");
        model.addAttribute(FRAGMENT_HTML_REPLACE_NAME,"planner");
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
        model.addAttribute(FRAGMENT_HTML_FILE,"plan_contents");
        model.addAttribute(FRAGMENT_HTML_REPLACE_NAME,"cash_flow");
        model.addAttribute("currentDate", LocalDate.now());

        return "plan";
    }


    @GetMapping("/planBudget")
    public String planBudget(Model model){
        model.addAttribute(FRAGMENT_HTML_FILE,"plan_contents");
        model.addAttribute(FRAGMENT_HTML_REPLACE_NAME,"budget");
        Authentication authentication = authenticationFacade.getAuthentication();
        model.addAttribute(LOGGED_USER, authentication.getName());
        return "plan";
    }

    @PostMapping("/addPlan")
    public String registerPlan(@ModelAttribute("planForm") PlanForm planForm){
        Authentication authentication = authenticationFacade.getAuthentication();


        Optional<String> error = plannedService.savePlannedOperation(planForm, authentication.getName());

        if(error.isPresent())
            logger.info(error.get());
        else
            logger.info("No error detected during plan saving process");

        return "redirect:/planExpenses";
    }

}