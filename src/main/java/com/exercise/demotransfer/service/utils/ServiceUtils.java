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

    public static boolean validateCurrency(String[] currencies, String inputCurrency){

        boolean isCurrencyOk = false;
        for (int i = 0; i < currencies.length; i++) {
            if (currencies[i].equals(inputCurrency)){
                isCurrencyOk = true;
                break;
            }
        }
        return isCurrencyOk;
    }

}
