package com.mariusz.home_budget.controler;

import com.mariusz.home_budget.entity.ShopingList;
import com.mariusz.home_budget.service.ShoppingService;
import com.mariusz.home_budget.service.ShoppingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@Controller
public class ShopingController {


    private final ShoppingServiceImpl shoppingService;

    @Autowired
    public ShopingController(ShoppingServiceImpl shoppingService) {
        this.shoppingService = shoppingService;
    }


//    @GetMapping("/")
//    public String helloWorld(){
//        return "main" ;
//    }

    @GetMapping("/shoppingList")
    public ShopingList getLastShoppingList(){
       return shoppingService.getLastShoppingList();
    }

}
