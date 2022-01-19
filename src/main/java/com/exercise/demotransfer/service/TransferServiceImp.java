package com.exercise.demotransfer.service;

import com.exercise.demotransfer.business.TransferInput;
import com.exercise.demotransfer.business.TransferOutput;
import com.exercise.demotransfer.data.entities.AccountEntity;
import com.exercise.demotransfer.data.repository.AccountRepository;
import com.exercise.demotransfer.data.repository.TransferRepository;
import com.google.gson.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
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
                }else {
                    if (transferInput.getCurrency().equals("USD")) {

                        //Calculate the tax
                        Double tax= calculateTax(transferInput.getAmount());


                        try {
                            //Convert USD to CAD
                            double cad = callApiExchangeRate(transferInput.getAmount());

                            //Save Transfer


                            //At the end of the transfer save the new balance
                            //Save accountOrigin with the new balance
                            accountOrigin.get().setAccountBalance(String.valueOf(Double.parseDouble(accountOrigin.get().getAccountBalance()) - transferInput.getAmount()-tax));
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
                        }

                    }else{
                        transferOutput.setStatus("ERROR");
                        errors.add("currency unsupported");
                        transferOutput.setErrors(errors);
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
                double eurs = amount/Double.parseDouble(usd);
                convertCad = eurs*Double.parseDouble(cad);
            }

        }

        return convertCad;

    }
}
