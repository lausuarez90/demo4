package com.exercise.demotransfer.business;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferInput {

    private int id;
    private BigDecimal amount;
    private String currency;
    private String origin_account;
    private String destination_account;
    private String description;

}
