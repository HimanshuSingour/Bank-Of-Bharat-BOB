package com.syc.finance.v1.bharat.controller;

import com.syc.finance.v1.bharat.dto.Accounts.Transaction.TransactionResponse;
import com.syc.finance.v1.bharat.service.TransactionService.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/transaction/v1/fetch")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/transaction-enquiry/{accountNumber}")
    ResponseEntity<List<TransactionResponse>> transactionGive(@PathVariable String accountNumber){
        List<TransactionResponse> response = transactionService.getAllTransaction(accountNumber);
        return new ResponseEntity<List<TransactionResponse>>(response , HttpStatus.ACCEPTED);
    }
}
