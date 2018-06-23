package com.mariusz.home_budget.controler;

import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.helpers.AuthenticationFacade;
import com.mariusz.home_budget.repository.UserRepository;
import com.mariusz.home_budget.service.FinancialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.Map;

@Controller
public class SiteLoaderController {
    private final AuthenticationFacade authenticationFacade;
    private final FinancialService financialService;
    private final UserRepository userRepository;

    @Autowired
    public SiteLoaderController(AuthenticationFacade authenticationFacade, FinancialService financialService, UserRepository userRepository) {
        this.authenticationFacade = authenticationFacade;
        this.financialService = financialService;
        this.userRepository = userRepository;
    }

    //Verification process done by Spring security.
    @GetMapping("/welcome")
    public String loginProccess(Model model){
        Authentication authentication = authenticationFacade.getAuthentication();
        AppUser user = userRepository.findByName(authentication.getName()).orElseThrow(()->new UsernameNotFoundException(""));
        String name = authentication.getName();
        model.addAttribute("loggedUser", name);
        Map<String, BigDecimal> balance = financialService.getBalance(user.getId());
        model.addAttribute("balance",balance.get("balance"));
        model.addAttribute("income",balance.get("income"));
        model.addAttribute("expense",balance.get("expense"));

        return "welcome";
    }

    @GetMapping("/analyze")
    public String getAnalyzePage (Model model){
        Authentication authentication = authenticationFacade.getAuthentication();
        model.addAttribute("loggedUser", authentication.getName());
        model.addAttribute("fragmentHtml","analyze_contents");
        model.addAttribute("fragment","empty");
        return "analyze";
    }

    @GetMapping("/plan")
    public String getPlanPage (Model model){
        Authentication authentication = authenticationFacade.getAuthentication();
        model.addAttribute("loggedUser", authentication.getName());
        return "plan";
    }

    @GetMapping("/settings")
    public String getSettingsPage (Model model){
        Authentication authentication = authenticationFacade.getAuthentication();
        model.addAttribute("loggedUser", authentication.getName());
        return "settings";
    }
}
