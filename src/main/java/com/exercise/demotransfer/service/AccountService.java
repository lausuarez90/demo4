package com.exercise.demotransfer.service;

import com.exercise.demotransfer.business.AccountOutput;

public interface AccountService {

    AccountOutput findBalanceAccount(String accountId);
}
