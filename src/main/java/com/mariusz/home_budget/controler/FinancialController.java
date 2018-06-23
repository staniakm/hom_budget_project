package com.mariusz.home_budget.controler;


import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.MoneyHolder;
import com.mariusz.home_budget.entity.entity_forms.MoneyFlowForm;
import com.mariusz.home_budget.helpers.AuthenticationFacade;
import com.mariusz.home_budget.repository.UserRepository;
import com.mariusz.home_budget.service.FinancialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class FinancialController {
    private final AuthenticationFacade authenticationFacade;
    private final FinancialService financialService;
    private final UserRepository userRepository;

    @Autowired
    public FinancialController(AuthenticationFacade authenticationFacade, FinancialService financialService
            , UserRepository userRepository) {
        this.authenticationFacade = authenticationFacade;
        this.financialService = financialService;
        this.userRepository = userRepository;
    }

    @GetMapping("/registerFlow")
    public String registerMoneyFlow(@RequestParam("val") String operation, Model model){
        model.addAttribute("operation", operation);
        model.addAttribute("currentDate", LocalDate.now());

        if (operation.equalsIgnoreCase("income") || operation.equalsIgnoreCase("expense")){
            Authentication authentication = authenticationFacade.getAuthentication();
            AppUser user = userRepository.findByName(authentication.getName()).orElseThrow(()->new UsernameNotFoundException(""));

            model.addAttribute("loggedUser", authentication.getName());

            MoneyFlowForm flowForm = new MoneyFlowForm();
            model.addAttribute("operationForm", flowForm);
            List<MoneyHolder> holders = financialService.getMoneyHolders(user);
            model.addAttribute("moneyHolders",holders);
            model.addAttribute("fragmentHtml","analyze_contents");
            model.addAttribute("fragment","addIncome");
        }
        return "analyze";
    }

    @PostMapping("/registerMoneyFlow")
    public String registerNewMoneyFlow(@ModelAttribute("operationForm") MoneyFlowForm newOperation
    ) {
        Authentication authentication = authenticationFacade.getAuthentication();

        newOperation.setUser(authentication.getName());

        Optional<String> errorOccur = financialService.addOperation(newOperation);
        if (errorOccur.isPresent()){
            return "redirect:/registerFlow?val="+newOperation.getOperation();
        }
        return "redirect:/welcome";
    }





}
