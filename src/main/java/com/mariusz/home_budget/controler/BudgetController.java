package com.mariusz.home_budget.controler;

import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.form.BudgetForm;
import com.mariusz.home_budget.helpers.AuthenticationFacade;
import com.mariusz.home_budget.service.FinancialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class BudgetController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final String LOGGED_USER = "loggedUser";
    private static final String FRAGMENT_HTML_REPLACE_NAME = "fragment";

    private final AuthenticationFacade authenticationFacade;
    private final FinancialService financialService;

    @Autowired
    public BudgetController( AuthenticationFacade authenticationFacade, FinancialService financialService) {

        this.authenticationFacade = authenticationFacade;
        this.financialService = financialService;
    }

    /**
     * Page that allow to create new planned budget for current month
     */
    @GetMapping("/planBudget")
    public String planBudget(Model model){
        AppUser user = authenticationFacade.getApplicationUser();
        model.addAttribute(FRAGMENT_HTML_REPLACE_NAME,"budget");
        model.addAttribute(LOGGED_USER, user);

        //TODO load categories from database
        List<String> categories = Arrays.asList("Samochód","Jedzenie","Rachunki");

        model.addAttribute("categories",categories);
        BudgetForm budgetForm = new BudgetForm();
        model.addAttribute("budgetForm",budgetForm);

        return "plan";
    }

    /**
     * Create new plan
     */
    @PostMapping("/addBudget")
    public String addBudget(@ModelAttribute("budgetForm") BudgetForm budgetForm){
        AppUser user = authenticationFacade.getApplicationUser();
        Optional<String> error = financialService.savePlannedBudget(budgetForm, user);
        if(error.isPresent())
            logger.info(error.get());
        else
            logger.info("No error detected during plan saving process");
        return "redirect:/planBudget";
    }

    @GetMapping("/recalculateBudget")
    public String recalculateBudget() {
        AppUser user = authenticationFacade.getApplicationUser();
        financialService.recalculateBudget(user);
        return "redirect:/summaryAnalyze";
    }
}
