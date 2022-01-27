package com.exercise.demotransfer.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertEquals;


@SpringBootTest
public class TransferServiceProperTest {

    @Autowired
    private TransferServiceImp transferServiceImp;

    @Test
    void setProperties(){
        String firstProperty = transferServiceImp.getCurrencySupported();
        String secondProperty = transferServiceImp.getAccessKey();

        assertEquals("file", firstProperty);
        assertEquals("file", secondProperty);
    }


}
