package com.exercise.demotransfer.business;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountInput implements Serializable {

    private int id;
    private String account_id;
    private Double account_balance;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public Double getAccount_balance() {
        return account_balance;
    }

    public void setAccount_balance(Double account_balance) {
        this.account_balance = account_balance;
    }
}
