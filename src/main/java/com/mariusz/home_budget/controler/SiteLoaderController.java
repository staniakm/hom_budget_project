package com.mariusz.home_budget.controler;

import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.PlannedOperation;
import com.mariusz.home_budget.helpers.AuthenticationFacade;
import com.mariusz.home_budget.repository.UserRepository;
import com.mariusz.home_budget.service.FinancialService;
import com.mariusz.home_budget.service.PlannedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
public class SiteLoaderController {
    private final AuthenticationFacade authenticationFacade;
    private final FinancialService financialService;
    private final UserRepository userRepository;
    private final PlannedService plannedService;

    @Autowired
    public SiteLoaderController(AuthenticationFacade authenticationFacade, FinancialService financialService, UserRepository userRepository, PlannedService plannedService) {
        this.authenticationFacade = authenticationFacade;
        this.financialService = financialService;
        this.userRepository = userRepository;
        this.plannedService = plannedService;
    }

    //Verification process done by Spring security.
    @GetMapping("/welcome")
    public String loginProcess(Model model){
        String userName = authenticationFacade.getAuthenticatedUser();
        AppUser user = userRepository.findByName(userName).orElseThrow(()->new UsernameNotFoundException(""));

        model.addAttribute("loggedUser", userName);
        Map<String, BigDecimal> balance = financialService.getBalance(user.getId());
        model.addAttribute("balance",balance.get("balance"));
        model.addAttribute("income",balance.get("income"));
        model.addAttribute("expense",balance.get("expense"));
        List<PlannedOperation> operations = plannedService.getPlanedActiveOperation(user);
        model.addAttribute("plannedOperations",operations);

        return "welcome";
    }

    @GetMapping("/analyze")
    public String getAnalyzePage (Model model){
        model.addAttribute("loggedUser", authenticationFacade.getAuthenticatedUser());
        model.addAttribute("fragmentHtml","analyze_contents");
        model.addAttribute("fragment","empty");
        return "analyze";
    }



    @GetMapping("/settings")
    public String getSettingsPage (Model model){
        model.addAttribute("loggedUser", authenticationFacade.getAuthenticatedUser());
        return "settings";
    }
}
