package com.exercise.demotransfer.business;

import lombok.Data;

import java.util.ArrayList;

@Data
public class OutputStatus {

    private String status;
    private ArrayList<String> errors;
}
