package com.exercise.demotransfer;

import com.exercise.demotransfer.business.AccountOutput;
import com.exercise.demotransfer.data.entities.AccountEntity;
import com.exercise.demotransfer.data.repository.AccountRepository;
import com.exercise.demotransfer.service.AccountServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DemoApplicationTests {

	private AccountServiceImp accountServiceImp;

	@Mock
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
		/*given(accountRepository.findByAccountId(accountId)).willReturn(Optional.empty());*/

		AccountEntity account = new AccountEntity();
		account.setId(1L);
		account.setAccountId("78523456");
		account.setAccountBalance("58236");
		/*given(accountRepository.save(account)).willReturn(account);*/

		Optional<AccountEntity> entity = Optional.of(account);
		when(accountRepository.findByAccountId(accountId)).thenReturn(entity);

		AccountOutput output = accountServiceImp.getBalanceAccount(accountId);
		assertEquals("OK", output.getStatus());

		/*assertEquals("8000", entity.get().getAccountBalance());



		/*then(output.getStatus().equals("OK"));

		when(output.getStatus().equals("ERROR")).thenReturn(false);
		when(output.getStatus().equals("OK")).thenReturn(true);
*/
	}

}
