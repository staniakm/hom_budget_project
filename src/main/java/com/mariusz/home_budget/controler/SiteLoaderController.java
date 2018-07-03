package com.mariusz.home_budget.controler;

import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.MonthKeeper;
import com.mariusz.home_budget.helpers.AuthenticationFacade;
import com.mariusz.home_budget.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SiteLoaderController {

    private static final String LOGGED_USER = "loggedUser";

    private final AuthenticationFacade authenticationFacade;
    private final FinancialService financialService;
    private final PlannedService plannedService;
    private final BudgetService budgetService;
    private final MessagesService messagesService;
    private final CurrencyService currencyService;



    @Autowired
    public SiteLoaderController(AuthenticationFacade authenticationFacade, FinancialService financialService
            , PlannedService plannedService, BudgetService budgetService, MessagesService messagesService, CurrencyService currencyService) {
        this.authenticationFacade = authenticationFacade;
        this.financialService = financialService;
        this.plannedService = plannedService;
        this.budgetService = budgetService;
        this.messagesService = messagesService;
        this.currencyService = currencyService;
    }

    //Verification process done by Spring security.
    @GetMapping(value = {"/welcome","/previousMonth","/nextMonth"})
    public String summaryPage(Model model, @RequestParam(value = "month", required = false) Integer month){

        AppUser user = authenticationFacade.getApplicationUser();

        model.addAttribute(LOGGED_USER, user.getName());

        model.addAttribute("balance",financialService.getCurrentMonthAccountBalance(user.getId()));
        model.addAttribute("plannedOperations",plannedService.getPlanedActiveOperation(user));

        MonthKeeper monthKeeper = new MonthKeeper(month, messagesService);
        model.addAttribute("month", monthKeeper);
        model.addAttribute("plannedBudgets", budgetService.getPlannedBudgets(user, monthKeeper.getCurrent()));
        model.addAttribute("currency", currencyService.getCurrences());

        model.addAttribute("accountSum",financialService.getTotalAmount(user));
        model.addAttribute("investmentSum",financialService.getInvestmentsSum(user));

        return "welcome";
    }


    @GetMapping("/settings")
    public String getSettingsPage (Model model){
        model.addAttribute(LOGGED_USER, authenticationFacade.getAuthenticatedUser());
        return "settings";
    }
}
