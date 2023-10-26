package com.syc.finance.v1.bharat.controller;


import com.syc.finance.v1.bharat.dto.TransferMoney.TransferMoneyRequest;
import com.syc.finance.v1.bharat.dto.TransferMoney.TransferMoneyResponse;
import com.syc.finance.v1.bharat.service.UpiAndNetBanking.UPIAndNetBankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("transfer/v1/banking/v6")
public class TransferAccount {

    @Autowired
    private UPIAndNetBankingService upiAndNetBankingService;

    @PostMapping("/process")
    ResponseEntity<TransferMoneyResponse> transactionGive(@RequestBody TransferMoneyRequest transferMoneyRequest){
        TransferMoneyResponse response = upiAndNetBankingService.moneyTransferAccountToAccount(transferMoneyRequest);
        return new ResponseEntity<TransferMoneyResponse>(response , HttpStatus.ACCEPTED);
    }
}
