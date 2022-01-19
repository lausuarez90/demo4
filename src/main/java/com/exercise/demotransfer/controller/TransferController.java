package com.exercise.demotransfer.controller;

import com.exercise.demotransfer.business.AccountInput;
import com.exercise.demotransfer.business.AccountOutput;
import com.exercise.demotransfer.business.TransferInput;
import com.exercise.demotransfer.business.TransferOutput;
import com.exercise.demotransfer.service.AccountService;
import com.exercise.demotransfer.service.TransferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/transfer")
@RestController
public class TransferController {

    @Autowired
    private TransferService transferService;


    @PostMapping
    public TransferOutput setTransfer(@RequestBody TransferInput transferInput) {
        return transferService.saveTransfer(transferInput);
    }
}
