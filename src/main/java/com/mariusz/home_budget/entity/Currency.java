package com.mariusz.home_budget.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Currency {
    private String table;
    private String currency;
    private String code;
    private Rate[] rates;


}
