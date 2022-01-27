package com.exercise.demotransfer.service;

import com.exercise.demotransfer.business.TransferInput;
import com.exercise.demotransfer.business.TransferOutput;

public interface TransferService {

    TransferOutput setTransfer(TransferInput transferInput);


}
