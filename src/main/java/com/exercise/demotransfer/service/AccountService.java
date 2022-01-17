package com.exercise.demotransfer.service;

import com.exercise.demotransfer.business.AccountOutput;
import com.exercise.demotransfer.data.entities.AccountEntity;

public interface AccountService {

    AccountOutput findBalanceAccount(String accountId);
}
