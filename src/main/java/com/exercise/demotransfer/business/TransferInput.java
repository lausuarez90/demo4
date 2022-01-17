package com.exercise.demotransfer.business;

public class TransferInput {

    private int id;
    private Double amount;
    private String currency;
    private String origin_account;
    private String destination_account;
    private String description;
    private Double tax_collected;
    private Double cad;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getOrigin_account() {
        return origin_account;
    }

    public void setOrigin_account(String origin_account) {
        this.origin_account = origin_account;
    }

    public String getDestination_account() {
        return destination_account;
    }

    public void setDestination_account(String destination_account) {
        this.destination_account = destination_account;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getTax_collected() {
        return tax_collected;
    }

    public void setTax_collected(Double tax_collected) {
        this.tax_collected = tax_collected;
    }

    public Double getCad() {
        return cad;
    }

    public void setCad(Double cad) {
        this.cad = cad;
    }
}
