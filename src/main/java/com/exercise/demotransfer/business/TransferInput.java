package com.exercise.demotransfer.business;

import lombok.Data;

@Data
public class TransferInput {

    private int id;
    private Double amount;
    private String currency;
    private String origin_account;
    private String destination_account;
    private String description;

}
