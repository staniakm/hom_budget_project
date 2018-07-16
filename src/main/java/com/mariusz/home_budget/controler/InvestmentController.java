package com.mariusz.home_budget.controler;

import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.Investment;
import com.mariusz.home_budget.entity.form.InvestmentForm;
import com.mariusz.home_budget.helpers.AuthenticationFacade;
import com.mariusz.home_budget.helpers.LengthKeeper;
import com.mariusz.home_budget.service.InvestmentService;
import com.mariusz.home_budget.service.MoneyHolderService;
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
public class InvestmentController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    //### HTML ###//
    private static final String ANALYZE_PAGE = "analyze";


    private final AuthenticationFacade authenticationFacade;
    private final InvestmentService investmentService;
    private final MoneyHolderService moneyHolderService;

    @Autowired
    public InvestmentController(AuthenticationFacade authenticationFacade, InvestmentService investmentService, MoneyHolderService moneyHolderService) {
        this.authenticationFacade = authenticationFacade;
        this.investmentService = investmentService;
        this.moneyHolderService = moneyHolderService;
    }


    @GetMapping("/summaryInvestment")
    public String summaryInvestment(Model model) {
        model.addAttribute("currentDate", LocalDate.now());

        AppUser user = authenticationFacade.getApplicationUser();
        model.addAttribute("loggedUser", user.getName());
        model.addAttribute("fragment", "show_investment_summary");

        model.addAttribute("nav", "investment_nav");
        List<Investment> activeInvestments = investmentService.getInvestments(user);
        model.addAttribute("investments", activeInvestments);
        model.addAttribute("investment",activeInvestments);
        model.addAttribute("investmentSum",investmentService.getInvestmentsSum(user));
        return ANALYZE_PAGE;
    }

    @GetMapping("/getInvestment")
    public String getInvestment(@RequestParam("val") Long id, Model model) {
        model.addAttribute("currentDate", LocalDate.now());
        AppUser user = authenticationFacade.getApplicationUser();
        model.addAttribute("loggedUser", user.getName());
        model.addAttribute("fragment", "show_investment_summary");
        model.addAttribute("nav", "investment_nav");

        model.addAttribute("investment", investmentService.getInvestmentsById(user, id));
        model.addAttribute("investments", investmentService.getInvestments(user));
        model.addAttribute("investmentSum", investmentService.getInvestmentsSum(user));

        return ANALYZE_PAGE;
    }

    @GetMapping("/registerInvestment")
    public String registerInvestment(Model model) {
        AppUser user = authenticationFacade.getApplicationUser();
        model.addAttribute("loggedUser", user.getName());
        model.addAttribute("fragment", "addInvestment");
        model.addAttribute("nav", "investment_nav");

        InvestmentForm investmentForm = new InvestmentForm();

        model.addAttribute("investmentForm", investmentForm);
        model.addAttribute("operators", Arrays.asList(LengthKeeper.values()));
        model.addAttribute("currentDate", LocalDate.now());
        model.addAttribute("investmentSum",investmentService.getInvestmentsSum(user));
        model.addAttribute("moneyHolders", moneyHolderService.getMoneyHolders(user));

        return ANALYZE_PAGE;
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

        Optional<String> error = investmentService.addInvestment(investmentForm, user);

        if (error.isPresent()) {
            return "redirect:/registerInvestment";
        }

        return "redirect:/summaryInvestment";
    }
}
