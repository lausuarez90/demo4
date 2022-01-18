package com.exercise.demotransfer.business;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountInput implements Serializable {

    private int id;
    private String account_id;


}
