package com.exercise.demotransfer.service.utils;

public class ServiceUtils {


    public static Double calculateTax(Double amount) {

        double tax;

        if (amount > 100) {
            tax = (amount * 0.5) / 100;
        } else {
            tax = (amount * 0.2) / 100;
        }
        return tax;
    }

}
