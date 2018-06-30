package com.mariusz.home_budget.service;

import com.mariusz.home_budget.entity.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessagesServiceImpl implements MessagesService {


    @Autowired
    Messages messages;

    @Override
    public String getMessage(String code) {
        String message = messages.get(code);
        if (message==null){
            message = "";
        }
        return message;
    }
}