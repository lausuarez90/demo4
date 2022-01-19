package com.exercise.demotransfer;

import com.exercise.demotransfer.data.entities.AccountEntity;
import com.exercise.demotransfer.data.repository.AccountRepository;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.annotation.Resource;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = { DemoJpaConfig.class },
        loader = AnnotationConfigContextLoader.class)
@Transactional
public class AccountTest {

    @Resource
    private AccountRepository accountRepository;

    @Test
    public void getAccountTest(){
        String accountId = "123456789";
        /*Optional<AccountEntity> entity = accountRepository.findByAccountId(accountId);
        assertEquals("50000", entity.get().getAccountBalance());*/

        AccountEntity account = new AccountEntity();
        //account.setId(6L);
        account.setAccountId("78523456");
        account.setAccountBalance("58236");
        account = accountRepository.save(account);

        Long newAccountId = account.getId();

        Optional<AccountEntity> newAccount = accountRepository.findByAccountId("78523456");
        assertEquals(newAccountId, newAccount.get().getId());




    }
}
