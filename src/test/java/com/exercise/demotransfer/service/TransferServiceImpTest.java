package com.exercise.demotransfer.service;

import com.exercise.demotransfer.business.TransferInput;
import com.exercise.demotransfer.business.TransferOutput;
import com.exercise.demotransfer.data.entities.AccountEntity;
import com.exercise.demotransfer.data.repository.AccountRepository;
import com.exercise.demotransfer.data.repository.TransferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest (value = { "currency.supported=USD;" })
class TransferServiceImpTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransferRepository transferRepository;

    @Autowired
    private TransferService transferService;

    private String currencySupported;

    @BeforeEach
    public void setUp(){

        //transferService = new TransferServiceImp(accountRepository, transferRepository);
        //currencySupported = env.getProperty("currency.supported");
        //when(System.getenv("currencySupported")).thenReturn("USD;");

    }

    @Test
    void setTransfer() {

        TransferInput transferInput = new TransferInput();
        transferInput.setAmount(new BigDecimal("500"));
        transferInput.setCurrency("USD");
        transferInput.setOrigin_account("789456123");
        transferInput.setDestination_account("852369741");
        transferInput.setDescription("Hey! I am sending your money, this is a test");


        AccountEntity account = new AccountEntity();
        account.setId(4L);
        account.setAccountId("789456123");
        account.setAccountBalance("25800");
        Optional<AccountEntity> entity = Optional.of(account);
        when(accountRepository.findByAccountId(transferInput.getOrigin_account())).thenReturn(entity);

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = formatter.format(date);
        try {
            //TODO traer la variable currencySupported
            //when (currencySupported).thenReturn("USD;");

            when(transferRepository.lastTransfer(transferInput.getOrigin_account(),new SimpleDateFormat("yyyy-MM-dd").parse(strDate))).thenReturn(0L);

            when(accountRepository.save(account)).thenReturn(account);

            TransferOutput output = transferService.setTransfer(transferInput);
            assertEquals("OK", output.getStatus());

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCallApiExchangeRate() {
    }
}