package com.exercise.demotransfer.business;

import lombok.Data;

import java.util.ArrayList;

@Data
public class AccountOutput extends AccountInput{

    private String status;
    private ArrayList<String> errors;


}
