package com.exercise.demotransfer.business;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferOutput  extends OutputStatus{

    private Double tax_collected;
    private BigDecimal cad;
}
