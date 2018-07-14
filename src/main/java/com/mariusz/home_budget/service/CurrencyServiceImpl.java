package com.mariusz.home_budget.service;

import com.google.common.collect.Lists;
import com.mariusz.home_budget.entity.Currency;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class CurrencyServiceImpl implements CurrencyService {
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
}
