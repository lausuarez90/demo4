package com.exercise.demotransfer.service.utils;

import com.exercise.demotransfer.business.TransferInput;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ServiceUtilsTest {

    @Autowired
    ServiceUtils serviceUtils;

    @Test
    void calculateTaxGreaterThan100() {

        TransferInput transferInput = new TransferInput();
        transferInput.setAmount(new BigDecimal("500"));
        transferInput.setCurrency("USD");
        transferInput.setOrigin_account("789456123");
        transferInput.setDestination_account("852369741");
        transferInput.setDescription("Hey! I am sending your money, this is a test");

        Double tax = ServiceUtils.calculateTax(transferInput.getAmount().doubleValue());
        assertEquals(2.5, tax);

    }

    @Test
    void calculateTaxLeesThan100() {

        TransferInput transferInput = new TransferInput();
        transferInput.setAmount(new BigDecimal("80"));
        transferInput.setCurrency("USD");
        transferInput.setOrigin_account("789456123");
        transferInput.setDestination_account("852369741");
        transferInput.setDescription("Hey! I am sending your money, this is a test");

        Double tax = ServiceUtils.calculateTax(transferInput.getAmount().doubleValue());
        assertEquals(0.16, tax);

    }

    @Test
    void validateCurrencyIsOK() {

        TransferInput transferInput = new TransferInput();
        transferInput.setAmount(new BigDecimal("80"));
        transferInput.setCurrency("USD");
        transferInput.setOrigin_account("789456123");
        transferInput.setDestination_account("852369741");
        transferInput.setDescription("Hey! I am sending your money, this is a test");

        String currencySupported = "USD;";
        String[] currencies = currencySupported.split(";");

        boolean isCurrencyOk = ServiceUtils.validateCurrency(currencies, transferInput.getCurrency());
        assertEquals(true, isCurrencyOk);


    }

    @Test
    void validateCurrencyIsFail() {

        TransferInput transferInput = new TransferInput();
        transferInput.setAmount(new BigDecimal("80"));
        transferInput.setCurrency("CAD");
        transferInput.setOrigin_account("789456123");
        transferInput.setDestination_account("852369741");
        transferInput.setDescription("Hey! I am sending your money, this is a test");

        String currencySupported = "USD;";
        String[] currencies = currencySupported.split(";");

        boolean isCurrencyOk = ServiceUtils.validateCurrency(currencies, transferInput.getCurrency());
        assertEquals(false, isCurrencyOk);


    }
}