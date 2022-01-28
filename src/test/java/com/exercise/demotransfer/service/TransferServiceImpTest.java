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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
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
import static org.junit.Assert.assertNotEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

//@ExtendWith(MockitoExtension.class)
@SpringBootTest
/*@SpringBootConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@PropertySource("classpath:application.properties")
@TestPropertySource("/application.properties")
@TestPropertySource(properties = {
        "currency.supported=USD;",
        "exchangeratesapi.host=http://api.exchangeratesapi.io/v1/latest?",
        "exchangeratesapi.accesskey=access_key=56e98d40b6310d0010cca501a64d0426",
        "exchangeratesapi.symbols=&symbols=USD,AUD,CAD,PLN,MXN"
})*/

class TransferServiceImpTest {

   /* @Value("${currency.supported}")
    private String currencySupported;*/

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransferRepository transferRepository;

    @Autowired
    private TransferService transferService;


    private TransferServiceImp transferServiceImp;

    @BeforeEach
    public void setUp(){

       // transferService = new TransferServiceImp(accountRepository, transferRepository, currencySupported);
        //transferServiceImp = new TransferServiceImp(accountRepository,transferRepository, currencySupported);
        //currencySupported = env.getProperty("currency.supported");
        //when(System.getenv("currencySupported")).thenReturn("USD;");

    }

    @Test
    void sureSetTransfer() {

        TransferInput transferInput = new TransferInput();
        transferInput.setAmount(new BigDecimal("500"));
        transferInput.setCurrency("USD");
        transferInput.setOrigin_account("789456123");
        transferInput.setDestination_account("852369741");
        transferInput.setDescription("Hey! I am sending your money, this is a test");

        TransferOutput output = transferService.setTransfer(transferInput);
        assertEquals("OK", output.getStatus());


        /*AccountEntity account = new AccountEntity();
        account.setId(4L);
        account.setAccountId("789456123");
        account.setAccountBalance("25800");
        Optional<AccountEntity> entity = Optional.of(account);
        when(accountRepository.findByAccountId(transferInput.getOrigin_account())).thenReturn(entity);

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = formatter.format(date);
        try {

            when(transferRepository.lastTransfer(transferInput.getOrigin_account(),new SimpleDateFormat("yyyy-MM-dd").parse(strDate))).thenReturn(0L);

            when(accountRepository.save(account)).thenReturn(account);

            TransferOutput output = transferService.setTransfer(transferInput);
            assertEquals("OK", output.getStatus());

        } catch (ParseException e) {
            e.printStackTrace();
        }*/
    }

    @Test
    void testOriginAccountDifferentDestinationAccount() {

        TransferInput transferInput = new TransferInput();
        transferInput.setAmount(new BigDecimal("500"));
        transferInput.setCurrency("USD");
        transferInput.setOrigin_account("789456123");
        transferInput.setDestination_account("789456123");
        transferInput.setDescription("Hey! I am sending your money, this is a test");

        TransferOutput output = transferService.setTransfer(transferInput);
        assertNotEquals("OK", output.getStatus(), "The origin account is equal to destination account");
    }

    @Test
    void testInsufficientFunds() {

        TransferInput transferInput = new TransferInput();
        transferInput.setAmount(new BigDecimal("10000"));
        transferInput.setCurrency("USD");
        transferInput.setOrigin_account("741258963");
        transferInput.setDestination_account("789456123");
        transferInput.setDescription("Hey! I am sending your money, this is a test");

        TransferOutput output = transferService.setTransfer(transferInput);
        assertNotEquals("OK", output.getStatus(), "insufficient-funds");

    }

    @Test
    void testLimitExceeded() {

        TransferInput transferInput = new TransferInput();
        transferInput.setAmount(new BigDecimal("500"));
        transferInput.setCurrency("USD");
        transferInput.setOrigin_account("789456123");
        transferInput.setDestination_account("852369741");
        transferInput.setDescription("Hey! I am sending your money, this is a test");

        TransferOutput output = transferService.setTransfer(transferInput);

        TransferInput transferInput2 = new TransferInput();
        transferInput.setAmount(new BigDecimal("50"));
        transferInput.setCurrency("USD");
        transferInput.setOrigin_account("789456123");
        transferInput.setDestination_account("852369741");
        transferInput.setDescription("Hey! I am sending your money, this is a test");

        TransferOutput output2 = transferService.setTransfer(transferInput);

        TransferInput transferInput3 = new TransferInput();
        transferInput.setAmount(new BigDecimal("1000"));
        transferInput.setCurrency("USD");
        transferInput.setOrigin_account("789456123");
        transferInput.setDestination_account("852369741");
        transferInput.setDescription("Hey! I am sending your money, this is a test");

        TransferOutput output3 = transferService.setTransfer(transferInput);

        TransferInput transferInput4 = new TransferInput();
        transferInput.setAmount(new BigDecimal("500"));
        transferInput.setCurrency("USD");
        transferInput.setOrigin_account("789456123");
        transferInput.setDestination_account("852369741");
        transferInput.setDescription("Hey! I am sending your money, this is a test");

        TransferOutput output4 = transferService.setTransfer(transferInput);
        assertEquals("limit_exceeded", output4.getErrors().get(0));

    }

    @Test
    void testCurrencyUnsupported() {

        TransferInput transferInput = new TransferInput();
        transferInput.setAmount(new BigDecimal("10000"));
        transferInput.setCurrency("CAD");
        transferInput.setOrigin_account("123456789");
        transferInput.setDestination_account("789456123");
        transferInput.setDescription("Hey! I am sending your money, this is a test");

        TransferOutput output = transferService.setTransfer(transferInput);
        assertEquals("currency unsupported", output.getErrors().get(0));

    }

    @Test
    void testOriginAccountNotExist() {

        TransferInput transferInput = new TransferInput();
        transferInput.setAmount(new BigDecimal("10000"));
        transferInput.setCurrency("USD");
        transferInput.setOrigin_account("1");
        transferInput.setDestination_account("789456123");
        transferInput.setDescription("Hey! I am sending your money, this is a test");

        TransferOutput output = transferService.setTransfer(transferInput);
        assertEquals("The origin account does not exist", output.getErrors().get(0));

    }

}