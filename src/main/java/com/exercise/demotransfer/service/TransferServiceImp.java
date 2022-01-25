package com.exercise.demotransfer.service;

import com.exercise.demotransfer.business.TransferInput;
import com.exercise.demotransfer.business.TransferOutput;
import com.exercise.demotransfer.business.enums.MessagesEnum;
import com.exercise.demotransfer.data.entities.AccountEntity;
import com.exercise.demotransfer.data.entities.TransferEntity;
import com.exercise.demotransfer.data.repository.AccountRepository;
import com.exercise.demotransfer.data.repository.TransferRepository;
import com.exercise.demotransfer.service.utils.ServiceUtils;
import com.google.gson.*;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TransferServiceImp implements TransferService{


    private AccountRepository accountRepository;
    private TransferRepository transferRepository;

    @Value( "${exchangeratesapi.host}" )
    private String host;

    @Value( "${exchangeratesapi.accesskey}" )
    private String accessKey;

    @Value( "${exchangeratesapi.symbols}" )
    private String symbols;

    @Autowired
    public TransferServiceImp(AccountRepository accountRepository, TransferRepository transferRepository){
        this.accountRepository = accountRepository;
        this.transferRepository= transferRepository;
    }

    @Override
    public TransferOutput setTransfer(TransferInput transferInput) {

        TransferOutput transferOutput = new TransferOutput();
        ArrayList<String> errors = new ArrayList<>();

        try {

            if (transferInput != null) {

                //Validate origin account different to destination account
                if(transferInput.getOrigin_account().equals(transferInput.getDestination_account())){
                    transferOutput.setStatus(MessagesEnum.ERROR.getDescription());
                    errors.add(MessagesEnum.SAME_ACCOUNTS.getDescription());
                    transferOutput.setErrors(errors);
                    transferOutput.setTax_collected(0.0);
                    return transferOutput;
                }

                //Get the origin account to validate its balance
                Optional<AccountEntity> accountOrigin = accountRepository.findByAccountId(transferInput.getOrigin_account());
                if (accountOrigin.isPresent()) {
                    if (Double.parseDouble(accountOrigin.get().getAccountBalance()) < transferInput.getAmount().doubleValue()) {
                        transferOutput.setStatus(MessagesEnum.ERROR.getDescription());
                        errors.add(MessagesEnum.INSUFFICIENT_FUNDS.getDescription());
                        transferOutput.setErrors(errors);
                        transferOutput.setTax_collected(0.0);
                    } else {
                        if (transferInput.getCurrency().equals("USD")) {

                            Date date = new Date();
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            String strDate = formatter.format(date);
                            //Get the max transfer to validate 3 max transfer per day
                            Long maxTransfer = transferRepository.lastTransfer(transferInput.getOrigin_account(), new SimpleDateFormat("yyyy-MM-dd").parse(strDate));
                            int counterTranfers = 0;
                            if (maxTransfer != null) {
                                counterTranfers = maxTransfer.intValue();
                            }

                            if (maxTransfer != null && maxTransfer >= 3) {
                                transferOutput.setStatus(MessagesEnum.ERROR.getDescription());
                                errors.add(MessagesEnum.LIMIT_EXCEEDED.getDescription());
                                transferOutput.setErrors(errors);
                                transferOutput.setTax_collected(0.0);

                            } else {

                                //Calculate the tax
                                Double tax = ServiceUtils.calculateTax(transferInput.getAmount().doubleValue());

                                try {
                                    //Convert USD to CAD
                                    BigDecimal cad = callApiExchangeRate(transferInput.getAmount().doubleValue());
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
                                    transfer.setDateTransfer(new Date());
                                    transferRepository.save(transfer);

                                    //At the end of the transfer save the new balance
                                    //Save accountOrigin with the new balance
                                    accountOrigin.get().setAccountBalance(String.valueOf(Double.parseDouble(accountOrigin.get().getAccountBalance()) - transferInput.getAmount().doubleValue() - tax));
                                    accountRepository.save(accountOrigin.get());
                                    //Save accountDestination with the increase in the balance
                                    Optional<AccountEntity> accountDestination = accountRepository.findByAccountId(transferInput.getDestination_account());
                                    accountDestination.get().setAccountBalance(String.valueOf(Double.parseDouble(accountDestination.get().getAccountBalance()) + transferInput.getAmount().doubleValue()));
                                    accountRepository.save(accountDestination.get());

                                    transferOutput.setStatus(MessagesEnum.OK.getDescription());
                                    transferOutput.setErrors(errors);
                                    transferOutput.setCad(cad);
                                    transferOutput.setTax_collected(tax);

                                } catch (UnirestException e) {
                                    e.printStackTrace();
                                    transferOutput.setStatus(MessagesEnum.ERROR.getDescription());
                                    errors.add(MessagesEnum.ERROR_CALL_API.getDescription());
                                    transferOutput.setErrors(errors);
                                    transferOutput.setTax_collected(0.0);
                                }
                            }

                        } else {
                            transferOutput.setStatus(MessagesEnum.ERROR.getDescription());
                            errors.add(MessagesEnum.CURRENCY_UNSUPPORTED.getDescription());
                            transferOutput.setErrors(errors);
                            transferOutput.setTax_collected(0.0);
                        }
                    }
                } else {
                    transferOutput.setStatus(MessagesEnum.ERROR.getDescription());
                    errors.add(MessagesEnum.ORIGIN_ACCOUNT_NOT_EXIST.getDescription());
                    transferOutput.setErrors(errors);
                    transferOutput.setTax_collected(0.0);

                }
            }

        } catch (Exception e) {
            errors.add(MessagesEnum.UNEXPECTETD_ERROR.getDescription() + " " + e.getMessage());
            transferOutput.setErrors(errors);
            transferOutput.setStatus(MessagesEnum.ERROR.getDescription());

        }
        return transferOutput;
    }

    public BigDecimal callApiExchangeRate(Double amount) throws UnirestException {

        BigDecimal convertCad = new BigDecimal("0.0");

        // Host url
        //String host = env.getProperty("exchangeratesapi.host"); //"http://api.exchangeratesapi.io/v1/";
        String charset = "UTF-8";
        // Headers for a request
        //String access_key = env.getProperty("exchangeratesapi.accesskey");
        //String key = "56e98d40b6310d0010cca501a64d0426";
        //String symbols = env.getProperty("exchangeratesapi.symbols");

        HttpResponse<JsonNode> response = Unirest.get(host + accessKey + symbols)
                .asJson();

        if (response.getStatus() == 200) {

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jp = new JsonParser();
            JsonObject object = jp.parse(response.getBody().toString()).getAsJsonObject();
            String base = object.get("base").getAsString();
            JsonElement rates = object.get("rates");
            JsonObject rateName = rates.getAsJsonObject();
            String usd = rateName.get("USD").getAsString();
            String cad = rateName.get("CAD").getAsString();

            if (base.equals("EUR")) {
                BigDecimal euros = new BigDecimal(Double.toString(amount / Double.parseDouble(usd)));
                convertCad = new BigDecimal(Double.toString(euros.doubleValue() * Double.parseDouble(cad)));
            }

        }

        return convertCad;

    }

}
