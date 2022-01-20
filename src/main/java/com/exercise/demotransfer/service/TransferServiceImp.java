package com.exercise.demotransfer.service;

import com.exercise.demotransfer.business.TransferInput;
import com.exercise.demotransfer.business.TransferOutput;
import com.exercise.demotransfer.data.entities.AccountEntity;
import com.exercise.demotransfer.data.entities.TransferEntity;
import com.exercise.demotransfer.data.repository.AccountRepository;
import com.exercise.demotransfer.data.repository.TransferRepository;
import com.google.gson.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransferServiceImp implements TransferService{

    @Autowired
    private final AccountRepository accountRepository;

    @Autowired
    private final TransferRepository transferRepository;

    @Override
    public TransferOutput saveTransfer(TransferInput transferInput) {

        TransferOutput transferOutput = new TransferOutput();
        ArrayList<String> errors = new ArrayList<>();

        if (transferInput != null){
            Optional<AccountEntity> accountOrigin = accountRepository.findByAccountId(transferInput.getOrigin_account());
            if (accountOrigin.isPresent()){
                if (Double.parseDouble(accountOrigin.get().getAccountBalance()) < transferInput.getAmount()){
                    transferOutput.setStatus("ERROR");
                    errors.add("insufficient-funds");
                    transferOutput.setErrors(errors);
                    transferOutput.setTax_collected(0.0);
                }else {
                    if (transferInput.getCurrency().equals("USD")) {

                        Long maxTransfer = transferRepository.lastTransfer(transferInput.getOrigin_account());
                        int counterTranfers = 0;
                        if (maxTransfer != null) {
                            counterTranfers = maxTransfer.intValue();
                        }

                        if (maxTransfer != null && maxTransfer >= 3) {
                            transferOutput.setStatus("ERROR");
                            errors.add("limit_exceeded");
                            transferOutput.setErrors(errors);
                            transferOutput.setTax_collected(0.0);

                        } else {

                            //Calculate the tax
                            Double tax = calculateTax(transferInput.getAmount());

                            try {
                                //Convert USD to CAD
                                double cad = callApiExchangeRate(transferInput.getAmount());
                                counterTranfers += 1;

                                //Save Transfer
                                TransferEntity transfer = new TransferEntity();
                                transfer.setAmount(transferInput.getAmount());
                                transfer.setCurrency(transferInput.getCurrency());
                                transfer.setOriginAccount(transferInput.getOrigin_account());
                                transfer.setDestinationAccount(transferInput.getDestination_account());
                                transfer.setDescription(transferInput.getDescription());
                                transfer.setTaxCollected(tax);
                                transfer.setCad(cad);
                                transfer.setNumberTransfer(counterTranfers);
                                transferRepository.save(transfer);

                                //At the end of the transfer save the new balance
                                //Save accountOrigin with the new balance
                                accountOrigin.get().setAccountBalance(String.valueOf(Double.parseDouble(accountOrigin.get().getAccountBalance()) - transferInput.getAmount() - tax));
                                accountRepository.save(accountOrigin.get());
                                //Save accountDestination with the increase in the balance
                                Optional<AccountEntity> accountDestination = accountRepository.findByAccountId(transferInput.getDestination_account());
                                accountDestination.get().setAccountBalance(String.valueOf(Double.parseDouble(accountDestination.get().getAccountBalance()) + transferInput.getAmount()));
                                accountRepository.save(accountDestination.get());

                                transferOutput.setStatus("OK");
                                transferOutput.setErrors(errors);
                                transferOutput.setCad(cad);
                                transferOutput.setTax_collected(tax);

                            } catch (UnirestException e) {
                                e.printStackTrace();
                                transferOutput.setStatus("ERROR");
                                errors.add("error calling the exchangerate api");
                                transferOutput.setErrors(errors);
                                transferOutput.setTax_collected(0.0);
                            }
                        }

                    }else{
                        transferOutput.setStatus("ERROR");
                        errors.add("currency unsupported");
                        transferOutput.setErrors(errors);
                        transferOutput.setTax_collected(0.0);
                    }
                }
            }
        }

        return transferOutput;
    }

    public Double calculateTax(Double amount) {

        Double tax;

        if (amount > 100) {
            tax = (amount * 0.5) / 100;
        } else {
            tax = (amount * 0.2) / 100;
        }
        return tax;
    }


    public Double callApiExchangeRate(Double amount) throws UnirestException {

        Double convertCad = 0.0;

        // Host url
        String host = "http://api.exchangeratesapi.io/v1/";
        String charset = "UTF-8";
        // Headers for a request
        String acces_key = "access_key=";
        String key = "56e98d40b6310d0010cca501a64d0426";

        HttpResponse<JsonNode> response = Unirest.get(host + "latest?" + acces_key + key + "&symbols=USD,AUD,CAD,PLN,MXN")
                .asJson();

        if (response.getStatus() == 200){

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jp = new JsonParser();
            JsonObject object = jp.parse(response.getBody().toString()).getAsJsonObject();
            String base = object.get("base").getAsString();
            JsonElement rates = object.get("rates") ;
            JsonObject rateName = rates.getAsJsonObject();
            String usd = rateName.get("USD").getAsString();
            String cad = rateName.get("CAD").getAsString();

            if (base.equals("EUR")){
                double euros = amount/Double.parseDouble(usd);
                convertCad = euros*Double.parseDouble(cad);
            }

        }

        return convertCad;

    }
}
