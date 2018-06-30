package com.mariusz.home_budget.controler;

import com.mariusz.home_budget.entity.*;
import com.mariusz.home_budget.helpers.AuthenticationFacade;
import com.mariusz.home_budget.service.*;
import org.springframework.beans.factory.annotation.Autowired;
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

        String userName = authenticationFacade.getAuthenticatedUser();
        AppUser user = authenticationFacade.getApplicationUser();

        model.addAttribute(LOGGED_USER, userName);

        //TODO move to separated method that will return object with proper fields
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

        List<Currency> currencyList = currencyService.getCurrences();
        model.addAttribute("currency", currencyList);

        return "welcome";
    }

    @GetMapping("/analyze")
    public String getAnalyzePage (Model model){
        model.addAttribute(LOGGED_USER, authenticationFacade.getAuthenticatedUser());
        model.addAttribute("fragmentHtml","analyze_contents");
        model.addAttribute("fragment","empty");
        return "redirect:/summaryAnalyze";
    }



    @GetMapping("/settings")
    public String getSettingsPage (Model model){
        model.addAttribute(LOGGED_USER, authenticationFacade.getAuthenticatedUser());
        return "settings";
    }
}
