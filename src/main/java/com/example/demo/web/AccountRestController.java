package com.example.demo.web;

import com.example.demo.entities.AppUser;
import com.example.demo.service.AccountService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AccountRestController {

    private AccountService accountService;
    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }

     @GetMapping("/users")
    private List<AppUser> appUserList(){
        return  accountService.USER_LIST();
    }

}
