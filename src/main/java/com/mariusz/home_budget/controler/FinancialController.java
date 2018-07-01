package com.mariusz.home_budget.controler;


import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.Investment;
import com.mariusz.home_budget.entity.MoneyHolder;
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
public class FinancialController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final AuthenticationFacade authenticationFacade;
    private final FinancialService financialService;

    @Autowired
    public FinancialController(AuthenticationFacade authenticationFacade, FinancialService financialService
    ) {
        this.authenticationFacade = authenticationFacade;
        this.financialService = financialService;
    }

    @GetMapping("/registerFlow")
    public String registerMoneyFlow(@RequestParam("val") String operation
            , Model model) {
        model.addAttribute("operation", operation);
        model.addAttribute("currentDate", LocalDate.now());
        if (operation.equalsIgnoreCase("income") || operation.equalsIgnoreCase("expense")) {
            AppUser user = authenticationFacade.getApplicationUser();
            model.addAttribute("loggedUser", user.getName());

            MoneyFlowForm flowForm = new MoneyFlowForm();
            model.addAttribute("operationForm", flowForm);
            List<MoneyHolder> holders = financialService.getMoneyHolders(user);
            model.addAttribute("moneyHolders", holders);
            model.addAttribute("fragmentHtml", "analyze_contents");
            model.addAttribute("fragment", "addIncome");

            List<String> categories = Arrays.asList("Nieokreślona", "Samochód", "Jedzenie", "Rachunki");
            model.addAttribute("categories", categories);
        }
        return "analyze";
    }

    @GetMapping("/summaryAnalyze")
    public String summaryAnalyze(Model model) {
        model.addAttribute("currentDate", LocalDate.now());

        AppUser user = authenticationFacade.getApplicationUser();
        model.addAttribute("loggedUser", user.getName());
        model.addAttribute("fragmentHtml", "analyze_contents");
        model.addAttribute("fragment", "show_account_summary");

        List<MoneyFlowForm> moneyFlows = financialService.getMoneyFlows(user);
        model.addAttribute("nav", "account_nav");

        return "analyze";
    }


    @PostMapping("/registerMoneyFlow")
    public String registerNewMoneyFlow(@Valid MoneyFlowForm newOperation, BindingResult bindingResult, Model model
    ) {

        if (bindingResult.hasErrors()) {
            for (int i = 0; i < bindingResult.getErrorCount(); i++) {
                logger.info(bindingResult.getAllErrors().get(i).toString());
            }
            model.addAttribute("message", "Validation errors");
            return "redirect:/registerFlow?val=" + newOperation.getOperation();
        }

        AppUser user = authenticationFacade.getApplicationUser();

        newOperation.setUser(user);

        Optional<String> errorOccur = financialService.addOperation(newOperation);
        if (errorOccur.isPresent()) {
            return "redirect:/registerFlow?val=" + newOperation.getOperation();
        }
        return "redirect:/welcome";
    }


    @GetMapping("/summaryInvestment")
    public String summaryInvestment(Model model) {
        model.addAttribute("currentDate", LocalDate.now());

        AppUser user = authenticationFacade.getApplicationUser();
        model.addAttribute("loggedUser", user.getName());
        model.addAttribute("fragmentHtml", "analyze_contents");
        model.addAttribute("fragment", "show_investment_summary");

        model.addAttribute("nav", "investment_nav");
        List<Investment> activeInvestments = financialService.getInvestments(user);
        model.addAttribute("investments", activeInvestments);
        return "analyze";
    }

    @GetMapping("/registerInvestment")
    public String registerInvestment(Model model) {
        AppUser user = authenticationFacade.getApplicationUser();
        model.addAttribute("loggedUser", user.getName());
        model.addAttribute("fragmentHtml", "analyze_contents");
        model.addAttribute("fragment", "addInvestment");
        model.addAttribute("nav", "investment_nav");

        InvestmentForm investmentForm = new InvestmentForm();

        model.addAttribute("investmentForm", investmentForm);
        List<LengthKeeper> operators = Arrays.asList(LengthKeeper.values());
        model.addAttribute("operators", operators);
        model.addAttribute("currentDate", LocalDate.now());

        return "analyze";
    }

    @PostMapping("/registerInvestment")
    public String registerNewInvestment(@Valid InvestmentForm investmentForm
            , BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            for (int i = 0; i < bindingResult.getErrorCount(); i++) {
                logger.info(bindingResult.getAllErrors().get(i).toString());
            }
            model.addAttribute("message", "Validation errors");
            return "redirect:/registerInvestment";
        }

        AppUser user = authenticationFacade.getApplicationUser();

        Optional<String> error = financialService.addInvestment(investmentForm, user);

        if (error.isPresent()) {
            return "redirect:/registerInvestment";
        }

        return "redirect:/welcome";
    }


}
