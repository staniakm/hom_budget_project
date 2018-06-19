package com.mariusz.home_budget.controler;


import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.entity_forms.MoneyFlowForm;
import com.mariusz.home_budget.helpers.AuthenticationFacade;
import com.mariusz.home_budget.repository.UserRepository;
import com.mariusz.home_budget.service.FinancialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Controller
public class FinancialController {
    private final AuthenticationFacade authenticationFacade;
    private final FinancialService financialService;
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public FinancialController(AuthenticationFacade authenticationFacade, FinancialService financialService, UserRepository userRepository) {
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

    @GetMapping("/registerFlow")
    public String registerMoneyFlow(@RequestParam("val") String operation, Model model){
        model.addAttribute("operation", operation);
        model.addAttribute("currentDate", LocalDate.now());
        if (operation.equalsIgnoreCase("income") || operation.equalsIgnoreCase("expense")){
            MoneyFlowForm flowForm = new MoneyFlowForm();
            model.addAttribute("operationForm", flowForm);
        }
        return "registerMoneyFlow";
    }

    @PostMapping("/registerMoneyFlow")
    public String registerNewMoneyFlow(Model model , @ModelAttribute("operationForm") MoneyFlowForm newOperation
            , BindingResult result, HttpServletRequest request) {
        Authentication authentication = authenticationFacade.getAuthentication();

        newOperation.setUser(authentication.getName());

        Optional<String> errorOccur = financialService.addOperation(newOperation);

        //
        if (errorOccur.isPresent()){
        return "redirect:/registerFlow?val="+newOperation.getOperation();
        }
        return "redirect:/welcome";
    }

}
