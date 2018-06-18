package com.mariusz.home_budget.controler;


import com.mariusz.home_budget.helpers.AuthenticationFacade;
import com.mariusz.home_budget.service.FinancialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.Map;

@Controller
public class FinancialController {
    private final AuthenticationFacade authenticationFacade;
    private final FinancialService financialService;

    @Autowired
    public FinancialController(AuthenticationFacade authenticationFacade, FinancialService financialService) {
        this.authenticationFacade = authenticationFacade;
        this.financialService = financialService;
    }


    //Verification process done by Spring security.
    @GetMapping("/welcome")
    public String loginProccess(Model model){
        Authentication authentication = authenticationFacade.getAuthentication();
        String name = authentication.getName();
        model.addAttribute("loggedUser", name);
        Map<String, BigDecimal> balance = financialService.getBalance();
        model.addAttribute("income",balance.get("income"));
        model.addAttribute("expense",balance.get("expense"));

        return "welcome";
    }
}
