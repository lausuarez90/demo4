package com.exercise.demotransfer.service;

import com.exercise.demotransfer.business.AccountOutput;
import com.exercise.demotransfer.data.entities.AccountEntity;
import com.exercise.demotransfer.data.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountServiceImp implements AccountService{

    @Autowired
    private final AccountRepository accountRepository;

    @Override
    public AccountOutput findBalanceAccount(String accountId) {

        AccountOutput output = new AccountOutput();
        ArrayList<String> errors = new ArrayList<>();

        try {

            Optional<AccountEntity> account = accountRepository.findByAccountId(accountId);

            if (account.isPresent()) {

                if (Double.parseDouble(account.get().getAccountBalance()) < 0) {
                    errors.add("The account does not have balance");
                    output.setErrors(errors);
                    output.setStatus("ERROR");
                } else {
                    output.setStatus("OK");
                    output.setErrors(errors);
                    output.setAccount_balance(Double.parseDouble(account.get().getAccountBalance()));
                }

            } else {
                errors.add("The account does not exist");
                output.setErrors(errors);
                output.setStatus("ERROR");
            }

        } catch (Exception e) {
            errors.add("An error occurred " + e.getMessage());
            output.setErrors(errors);
            output.setStatus("ERROR");
        }

        return output;


    }
}
