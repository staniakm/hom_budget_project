package com.mariusz.home_budget.controler;

import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.MoneyHolder;
import com.mariusz.home_budget.entity.form.BudgetForm;
import com.mariusz.home_budget.entity.form.PlanForm;
import com.mariusz.home_budget.helpers.AuthenticationFacade;
import com.mariusz.home_budget.helpers.MoneyFlowTypes;
import com.mariusz.home_budget.helpers.PeriodicTypes;
import com.mariusz.home_budget.service.BudgetService;
import com.mariusz.home_budget.service.FinancialService;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class PlanController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String LOGGED_USER = "loggedUser";
    private static final String FRAGMENT_HTML_REPLACE_NAME = "fragment";


    private final PlannedService plannedService;
    private final AuthenticationFacade authenticationFacade;
    private final FinancialService financialService;
    private final BudgetService budgetService;

    @Autowired
    public PlanController(PlannedService plannedService, AuthenticationFacade authenticationFacade
            , FinancialService financialService, BudgetService budgetService) {

        this.plannedService = plannedService;
        this.authenticationFacade = authenticationFacade;
        this.financialService = financialService;
        this.budgetService = budgetService;
    }

    @GetMapping("/plan")
    public String getPlanPage (Model model){
        AppUser user = authenticationFacade.getApplicationUser();
        model.addAttribute(LOGGED_USER, user.getName());
        model.addAttribute(FRAGMENT_HTML_REPLACE_NAME,"empty_content");
        return "plan";
    }

    @GetMapping("/planExpenses")
    public String planExpenses(Model model){
        AppUser user = authenticationFacade.getApplicationUser();
        model.addAttribute(FRAGMENT_HTML_REPLACE_NAME,"planner");
        model.addAttribute(LOGGED_USER, user.getName());



        PlanForm planForm = new PlanForm();
        model.addAttribute("planForm", planForm);
        model.addAttribute("operators", Arrays.asList(PeriodicTypes.values()));
        model.addAttribute("currentDate",LocalDate.now());
        model.addAttribute("moneyFlowType",Arrays.asList(MoneyFlowTypes.values()));
        model.addAttribute("moneyHolders",financialService.getMoneyHolders(user));

        return "plan";
    }


    @GetMapping("/planCashFlow")
    public String planCashFlow(Model model){
        AppUser user = authenticationFacade.getApplicationUser();
        model.addAttribute(LOGGED_USER, user.getName());
        model.addAttribute(FRAGMENT_HTML_REPLACE_NAME,"cash_flow");
        model.addAttribute("currentDate", LocalDate.now());

        return "plan";
    }


    @GetMapping("/planBudget")
    public String planBudget(Model model){
        AppUser user = authenticationFacade.getApplicationUser();
        model.addAttribute(FRAGMENT_HTML_REPLACE_NAME,"budget");
        model.addAttribute(LOGGED_USER, user);

        List<String> categories = Arrays.asList("Samochód","Jedzenie","Rachunki");

        model.addAttribute("categories",categories);
        BudgetForm budgetForm = new BudgetForm();
        model.addAttribute("budgetForm",budgetForm);

        return "plan";
    }

    @PostMapping("/addBudget")
        public String addBudget(@ModelAttribute("budgetForm") BudgetForm budgetForm){
        AppUser user = authenticationFacade.getApplicationUser();
        Optional<String> error = budgetService.savePlannedBudget(budgetForm, user);
        if(error.isPresent())
            logger.info(error.get());
        else
            logger.info("No error detected during plan saving process");
        return "redirect:/planBudget";
    }

    @PostMapping("/addPlan")
    public String registerPlan(@ModelAttribute("planForm") PlanForm planForm){
        AppUser user = authenticationFacade.getApplicationUser();
        Optional<String> error = plannedService.savePlannedOperation(planForm, user);
        if(error.isPresent())
            logger.info(error.get());
        else
            logger.info("No error detected during plan saving process");
        return "redirect:/planExpenses";
    }

    @PostMapping("/finishPlan")
    public String finishOperation(@RequestParam("operationId") Long id){
        AppUser user = authenticationFacade.getApplicationUser();
        plannedService.finishPlan(id, user);
        return "redirect:/welcome";
    }

    @PostMapping("/deletePlan")
    public String deleteOperation(@RequestParam("operationId") Long id){
        AppUser user = authenticationFacade.getApplicationUser();
        plannedService.deletePlan(id, user);
        return "redirect:/welcome";
    }


}
