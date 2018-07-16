package com.mariusz.home_budget.controler;

import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.form.PlanForm;
import com.mariusz.home_budget.helpers.AuthenticationFacade;
import com.mariusz.home_budget.helpers.MoneyFlowTypes;
import com.mariusz.home_budget.helpers.PeriodicTypes;
import com.mariusz.home_budget.service.FinancialService;
import com.mariusz.home_budget.service.MoneyHolderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

@Controller
public class PlanController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String LOGGED_USER = "loggedUser";
    private static final String FRAGMENT_HTML_REPLACE_NAME = "fragment";

    private final AuthenticationFacade authenticationFacade;
    private final FinancialService financialService;
    private final MoneyHolderService moneyHolderService;

    @Autowired
    public PlanController(AuthenticationFacade authenticationFacade, FinancialService financialService, MoneyHolderService moneyHolderService) {

        this.authenticationFacade = authenticationFacade;
        this.financialService = financialService;
        this.moneyHolderService = moneyHolderService;
    }

    /**
     * Load main plan page
     *
     */
    @GetMapping("/plan")
    public String getPlanPage (Model model){
        AppUser user = authenticationFacade.getApplicationUser();
        model.addAttribute(LOGGED_USER, user.getName());
        model.addAttribute(FRAGMENT_HTML_REPLACE_NAME,"empty_content");
        return "plan";
    }

    /**
     * return page that allow to register new planned operation
     * PlanForm object linked with html form
     *
     */
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
        model.addAttribute("moneyHolders",moneyHolderService.getMoneyHolders(user));

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

    /**
     * Save new planned operation
     * @param planForm - simple form for planned operation
     *
     */
    @PostMapping("/addPlan")
    public String registerPlan(@ModelAttribute("planForm") PlanForm planForm){
        AppUser user = authenticationFacade.getApplicationUser();
        Optional<String> error = financialService.savePlannedOperation(planForm, user);
        if(error.isPresent())
            logger.info(error.get());
        else
            logger.info("No error detected during plan saving process");
        return "redirect:/planExpenses";
    }

    /**
     * Finish planned operation. Founds will be transfer to linked money holder
     * @param operationId - operation id
     *
     */
    @PostMapping("/finishPlan")
    public String finishOperation(@RequestParam("operationId") Long operationId){
        AppUser user = authenticationFacade.getApplicationUser();
        financialService.finishPlan(operationId, user);
        return "redirect:/welcome";
    }

    /**
     * Delete planed operation. Money will not be transferred to money holder.
     * @param operationId - planned operation id
     *
     */
    @PostMapping("/deletePlan")
    public String deleteOperation(@RequestParam("operationId") Long operationId){
        AppUser user = authenticationFacade.getApplicationUser();
        financialService.deletePlan(operationId, user);
        return "redirect:/welcome";
    }


}
