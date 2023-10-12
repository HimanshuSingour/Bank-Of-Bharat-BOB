package com.syc.finance.v1.bharat.controller;


import com.syc.finance.v1.bharat.dto.Accounts.Transaction.TransactionResponse;
import com.syc.finance.v1.bharat.dto.UPI.UPIRequest;
import com.syc.finance.v1.bharat.dto.UPI.UPIResponse;
import com.syc.finance.v1.bharat.service.TransactionService;
import com.syc.finance.v1.bharat.service.UpiAndNetBanking.UPIAndBankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("finance/upi/v1/")
public class UpiController {

    @Autowired
    private UPIAndBankingService upiAndBankingService;

    @PostMapping("/upi-create")
    ResponseEntity<UPIResponse> upiCreateUp(@RequestBody UPIRequest upiRequest){
        UPIResponse response = upiAndBankingService.upiCreate(upiRequest);
        return new ResponseEntity<UPIResponse>(response , HttpStatus.ACCEPTED);
    }

}
