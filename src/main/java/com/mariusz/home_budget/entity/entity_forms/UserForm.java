package com.mariusz.home_budget.entity.entity_forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserForm {

    private String name;
    private String password;
    private String confirmedPassword;


}
