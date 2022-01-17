package com.exercise.demotransfer;

import com.exercise.demotransfer.data.entities.AccountEntity;
import com.exercise.demotransfer.data.repository.AccountRepository;

import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.annotation.Resource;


import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        classes = { DemoJpaConfig.class },
        loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext
public class AccountTest {

    @Resource
    private AccountRepository accountRepository;

    @Test
    public void getAccountTest(){
        String accountId = "123456789";
        AccountEntity entity = accountRepository.findAccountByAccountId(accountId);
        assertEquals("50000", entity.getAccountBalance());

    }
}
