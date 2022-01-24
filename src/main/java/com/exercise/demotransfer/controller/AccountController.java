package com.exercise.demotransfer.controller;

import com.exercise.demotransfer.business.AccountInput;
import com.exercise.demotransfer.business.AccountOutput;
import com.exercise.demotransfer.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/accounts")
@RestController
public class AccountController {


    private AccountService accountService;

    @Autowired
    public AccountController (AccountService accountService){
        this.accountService = accountService;
    }

    @PostMapping
    public AccountOutput getBalanceAccount(@RequestBody AccountInput accountInput) {
        return accountService.getBalanceAccount(accountInput.getAccount_id());
    }
}
