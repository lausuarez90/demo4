package com.exercise.demotransfer.service;

import com.exercise.demotransfer.business.AccountOutput;
import com.exercise.demotransfer.data.entities.AccountEntity;
import com.exercise.demotransfer.data.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountServiceImp implements AccountService{

    private final AccountRepository accountRepository;

    @Override
    public AccountOutput findBalanceAccount(String accountId) {

        AccountOutput output = new AccountOutput();

        AccountEntity account = accountRepository.findAccountByAccountId(accountId);

        if (Double.parseDouble(account.getAccountBalance()) < 0){
            ArrayList<String> errors = new ArrayList<>();
            errors.add("The account does not have balance");
            output.setErrors(errors);
            output.setStatus("ERROR");
        }

        return output;
    }
}
