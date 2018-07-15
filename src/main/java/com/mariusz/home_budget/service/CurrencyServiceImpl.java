package com.mariusz.home_budget.service;

import com.google.common.collect.Lists;
import com.mariusz.home_budget.entity.AppUser;
import com.mariusz.home_budget.entity.Currency;
import com.mariusz.home_budget.repository.FinancialCustomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    @Autowired
    private FinancialCustomRepository customRepository;

    @Override
    public List<Currency> getCurrences() {
        List<Currency> curencyList = new ArrayList<>();
        List<String> URLs = Lists.newArrayList(
                "http://api.nbp.pl/api/exchangerates/rates/a/eur/?format=json",
                "http://api.nbp.pl/api/exchangerates/rates/a/usd/?format=json",
                "http://api.nbp.pl/api/exchangerates/rates/a/chf/?format=json");
        RestTemplate restTemplate = new RestTemplate();

        for (String url: URLs
             ) {
            curencyList.add(restTemplate.getForObject(url,Currency.class));
        }

        return curencyList;

    }

    @Override
    public List<Currency> getCurrences(AppUser user) {
        List<Currency> list;
        List<String> curencies = customRepository.getCurrenciesId(user);
        List<Integer> params = new ArrayList<>();
        if (curencies.size()>0 && curencies.get(0).trim().length()>0){
            String[] ids = curencies.get(0).split(",");
            for (String s: ids
                 ) {
                params.add(Integer.valueOf(s));
            }
            list = customRepository.getListOfCurencies(params);
        }else {
            list = new ArrayList<>();
        }
        return list;
    }

    @Override
    public Currency getCurrency(String link) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(link,Currency.class);
    }

}
