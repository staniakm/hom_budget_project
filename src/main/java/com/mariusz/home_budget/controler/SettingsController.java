package com.mariusz.home_budget.controler;

import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.form.WalletForm;
import com.mariusz.home_budget.helpers.AuthenticationFacade;
import com.mariusz.home_budget.helpers.MoneyHolderType;
import com.mariusz.home_budget.service.FinancialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class SettingsController {
    private final AuthenticationFacade authenticationFacade;
    private final FinancialService financialService;

    @Autowired
    public SettingsController(AuthenticationFacade authenticationFacade, FinancialService financialService) {
        this.authenticationFacade = authenticationFacade;
        this.financialService = financialService;
    }

    @GetMapping("/addWallet")
    public String addWallet (Model model){
        Authentication authentication = authenticationFacade.getAuthentication();
        model.addAttribute("loggedUser", authentication.getName());
        model.addAttribute("fragmentHtml","settings_content");
        model.addAttribute("fragment","addWallet");

        WalletForm walletForm = new WalletForm();
        model.addAttribute("walletForm", walletForm);
        List<MoneyHolderType> operators = Arrays.asList(MoneyHolderType.values());
        model.addAttribute("operators", operators);

        return "settings";
    }

    @PostMapping("/addWallet")
    public String registerNewWallet(@ModelAttribute("walletForm") WalletForm walletForm) {
        AppUser user = authenticationFacade.getApplicationUser();

        walletForm.setUser(user.getName());
        Optional<String> errorOccur = financialService.addMoneyHolder(walletForm, user);

        if (errorOccur.isPresent()){
            return "redirect:/settings";
        }
        return "redirect:/welcome";
    }
}
