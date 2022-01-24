package com.exercise.demotransfer.business.enums;

public enum MessagesEnum {

    OK("OK"),
    ERROR("ERROR"),

    //ACCOUNT ERRORS
    NOT_BALANCE("The account does not have balance"),
    ACCOUNT_NOT_EXIST("The account does not exist"),
    UNEXPECTETD_ERROR("An error occurred"),

    //TRANSFER ERRORS
    SAME_ACCOUNTS("Origin account is equal to Destination account"),
    INSUFFICIENT_FUNDS("insufficient-funds"),
    LIMIT_EXCEEDED("limit_exceeded"),
    ERROR_CALL_API("Error calling the exchangerate api"),
    CURRENCY_UNSUPPORTED("currency unsupported"),
    ORIGIN_ACCOUNT_NOT_EXIST("The origin account does not exist")

    ;

    private String description;

    MessagesEnum(String description) {
       this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
