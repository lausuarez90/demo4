package com.exercise.demotransfer;

import com.exercise.demotransfer.business.AccountOutput;
import com.exercise.demotransfer.data.entities.AccountEntity;
import com.exercise.demotransfer.data.repository.AccountRepository;
import com.exercise.demotransfer.service.AccountServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
		classes = { DemoJpaConfig.class },
		loader = AnnotationConfigContextLoader.class)
@Transactional
class DemoApplicationTests {

	private AccountServiceImp accountServiceImp;

	@Resource
	private AccountRepository accountRepository;

	@BeforeEach
	public void setUp(){
		accountServiceImp = new AccountServiceImp(accountRepository);
	}
	@Test
	void contextLoads() {
	}

	@Test
	public void checkGetAccount(){
		String accountId = "123456789";
		//given(accountRepository.findAccountByAccountId(accountId)).willReturn(accountRepository.findAccountByAccountId(accountId));

		AccountEntity entity = accountRepository.findAccountByAccountId(accountId);
		assertEquals("8000", entity.getAccountBalance());

		AccountOutput output = accountServiceImp.findBalanceAccount(accountId);
		assertEquals("OK", output.getStatus());

		/*then(output.getStatus().equals("OK"));

		when(output.getStatus().equals("ERROR")).thenReturn(false);
		when(output.getStatus().equals("OK")).thenReturn(true);
*/
	}

}
