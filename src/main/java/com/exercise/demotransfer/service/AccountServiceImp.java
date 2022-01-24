package com.exercise.demotransfer.service;

import com.exercise.demotransfer.business.AccountOutput;
import com.exercise.demotransfer.business.enums.MessagesEnum;
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

    private AccountRepository accountRepository;

    @Autowired
    public AccountServiceImp(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountOutput getBalanceAccount(String accountId) {

        AccountOutput output = new AccountOutput();
        ArrayList<String> errors = new ArrayList<>();

        try {

            Optional<AccountEntity> account = accountRepository.findByAccountId(accountId);

            if (account.isPresent()) {

                if (Double.parseDouble(account.get().getAccountBalance()) < 0) {
                    errors.add(MessagesEnum.NOT_BALANCE.getDescription());
                    output.setErrors(errors);
                    output.setStatus(MessagesEnum.ERROR.getDescription());
                } else {
                    output.setStatus(MessagesEnum.OK.getDescription());
                    output.setErrors(errors);
                    output.setAccount_balance(Double.parseDouble(account.get().getAccountBalance()));
                }

            } else {
                errors.add(MessagesEnum.ACCOUNT_NOT_EXIST.getDescription());
                output.setErrors(errors);
                output.setStatus(MessagesEnum.ERROR.getDescription());
            }

        } catch (Exception e) {
            errors.add(MessagesEnum.UNEXPECTETD_ERROR.getDescription() + " " + e.getMessage());
            output.setErrors(errors);
            output.setStatus(MessagesEnum.ERROR.getDescription());
        }

        return output;


    }
}
