package com.exercise.demotransfer.data.repository;

import com.exercise.demotransfer.data.entities.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, Long>  {

    Optional <AccountEntity> findByAccountId(String accountId);
    AccountEntity save (AccountEntity account);

}
