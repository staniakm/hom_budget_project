package com.mariusz.home_budget.controler;


import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.Investment;
import com.mariusz.home_budget.entity.form.InvestmentForm;
import com.mariusz.home_budget.entity.form.MoneyFlowForm;
import com.mariusz.home_budget.helpers.AuthenticationFacade;
import com.mariusz.home_budget.helpers.LengthKeeper;
import com.mariusz.home_budget.service.FinancialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class MoneyOperationController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    //### HTML ###//
    private static final String ANALYZE_PAGE = "analyze";

    private final AuthenticationFacade authenticationFacade;
    private final FinancialService financialService;

    @Autowired
    public MoneyOperationController(AuthenticationFacade authenticationFacade, FinancialService financialService
    ) {
        this.authenticationFacade = authenticationFacade;
        this.financialService = financialService;
    }

    @GetMapping("/registerOperation")
    public String registerMoneyFlow(@RequestParam("val") String operation
            , Model model) {
        model.addAttribute("operation", operation);
        model.addAttribute("currentDate", LocalDate.now());
        if (operation.equalsIgnoreCase("income") || operation.equalsIgnoreCase("expense")) {
            AppUser user = authenticationFacade.getApplicationUser();
            model.addAttribute("loggedUser", user.getName());
            model.addAttribute("fragment", "addIncome");
            model.addAttribute("nav", "account_nav");

            MoneyFlowForm flowForm = new MoneyFlowForm();
            model.addAttribute("operationForm", flowForm);
            model.addAttribute("moneyHolders", financialService.getMoneyHolders(user));
            model.addAttribute("accounts", financialService.getMoneyHolders(user));
            model.addAttribute("accountSum",financialService.getTotalAmount(user));

            //TODO load list form DB
            List<String> categories = Arrays.asList("Nieokreślona", "Samochód", "Jedzenie", "Rachunki");
            model.addAttribute("categories", categories);
        }
        return "analyze";
    }


    @GetMapping(value = {"/summaryAnalyze","/analyze"})
    public String summaryAnalyze(Model model) {
        model.addAttribute("currentDate", LocalDate.now());

        AppUser user = authenticationFacade.getApplicationUser();
        model.addAttribute("loggedUser", user.getName());
        model.addAttribute("fragment", "show_account_summary");
        model.addAttribute("nav", "account_nav");

        model.addAttribute("accounts",financialService.getMoneyHolders(user));
        model.addAttribute("accountSum",financialService.getTotalAmount(user));
        model.addAttribute("moneyFlows", financialService.getMoneyFlows(user));

        return ANALYZE_PAGE;
    }


    @PostMapping("/registerOperation")
    public String registerNewMoneyFlow(@Valid MoneyFlowForm newOperation, BindingResult bindingResult, Model model
    ) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("message", "Validation errors");
            return "redirect:/registerFlow?val=" + newOperation.getOperation();
        }

        AppUser user = authenticationFacade.getApplicationUser();

        Optional<String> errorOccur = financialService.addOperation(newOperation, user);
        if (errorOccur.isPresent()) {
            return "redirect:/registerFlow?val=" + newOperation.getOperation();
        }
        return "redirect:/summaryAnalyze";
    }

    @PostMapping("/deleteOperation")
    public String deleteOperation(@RequestParam("operationId") Long id,@RequestParam("operationType") String operationType){
        AppUser user = authenticationFacade.getApplicationUser();
        financialService.deleteMoneyOperation(id, user, operationType);
        return "redirect:/recalculateBudget";
    }

}
