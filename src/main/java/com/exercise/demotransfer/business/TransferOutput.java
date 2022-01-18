package com.exercise.demotransfer.business;

import lombok.Data;

import java.util.ArrayList;

@Data
public class TransferOutput  extends OutputStatus{

    private Double tax_collected;
    private Double cad;
}
