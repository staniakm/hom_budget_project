package com.mariusz.home_budget.controler;

import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.MonthKeeper;
import com.mariusz.home_budget.entity.PlannedBudget;
import com.mariusz.home_budget.entity.PlannedOperation;
import com.mariusz.home_budget.helpers.AuthenticationFacade;
import com.mariusz.home_budget.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
public class SiteLoaderController {

    private static final String LOGGED_USER = "loggedUser";

    private final AuthenticationFacade authenticationFacade;
    private final FinancialService financialService;
    private final ApplicationUserService userService;
    private final PlannedService plannedService;
    private final BudgetService budgetService;
    private final MessagesService messagesService;

    @Autowired
    public SiteLoaderController(AuthenticationFacade authenticationFacade, FinancialService financialService
            , ApplicationUserService userService, PlannedService plannedService, BudgetService budgetService, MessagesService messagesService) {
        this.authenticationFacade = authenticationFacade;
        this.financialService = financialService;
        this.userService = userService;
        this.plannedService = plannedService;
        this.budgetService = budgetService;
        this.messagesService = messagesService;
    }

    //Verification process done by Spring security.
    @GetMapping(value = {"/welcome","/previousMonth","/nextMonth"})
    public String summaryPage(Model model, @RequestParam(value = "month", required = false) Integer month){

        String userName = authenticationFacade.getAuthenticatedUser();
        AppUser user = userService.getUserByName(userName).orElseThrow(()->new UsernameNotFoundException(""));

        model.addAttribute(LOGGED_USER, userName);
        Map<String, BigDecimal> balance = financialService.getBalance(user.getId());
        model.addAttribute("balance",balance.get("balance"));
        model.addAttribute("income",balance.get("income"));
        model.addAttribute("expense",balance.get("expense"));
        List<PlannedOperation> operations = plannedService.getPlanedActiveOperation(user);
        model.addAttribute("plannedOperations",operations);


        MonthKeeper monthKeeper = new MonthKeeper(month, messagesService);
        model.addAttribute("month", monthKeeper);

        List<PlannedBudget> budgets = budgetService.getPlannedBudgets(user, monthKeeper.getCurrent());
        model.addAttribute("plannedBudgets", budgets);



        return "welcome";
    }

    @GetMapping("/analyze")
    public String getAnalyzePage (Model model){
        model.addAttribute(LOGGED_USER, authenticationFacade.getAuthenticatedUser());
        model.addAttribute("fragmentHtml","analyze_contents");
        model.addAttribute("fragment","empty");
        return "analyze";
    }



    @GetMapping("/settings")
    public String getSettingsPage (Model model){
        model.addAttribute(LOGGED_USER, authenticationFacade.getAuthenticatedUser());
        return "settings";
    }
}
