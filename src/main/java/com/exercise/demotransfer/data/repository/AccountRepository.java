package com.exercise.demotransfer.data.repository;

import com.exercise.demotransfer.data.entities.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<AccountEntity, Long>  {

    AccountEntity findAccountByAccountId(String accountId);

}
