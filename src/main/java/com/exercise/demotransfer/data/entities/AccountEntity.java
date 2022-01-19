package com.exercise.demotransfer.data.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "Account")
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String accountId;

    private String accountBalance;


    /*public AccountEntity(String accountId, String accountBalance){
        this.accountId = accountId;
        this.accountBalance = accountBalance;
    }*/
}
