package com.syc.finance.v1.bharat.controller;

import com.syc.finance.v1.bharat.dto.InternetBanking.NetBankingRequest;
import com.syc.finance.v1.bharat.dto.InternetBanking.NetBankingResponse;
import com.syc.finance.v1.bharat.service.UpiAndNetBanking.UPIAndBankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("finance/v1/banking")
public class NetBankingController {

    @Autowired
    private UPIAndBankingService upiAndBankingService;

    @PostMapping("/net-bankingId-create")
    ResponseEntity<NetBankingResponse> upiCreateUp(@RequestBody NetBankingRequest netBankingRequest){
        NetBankingResponse response = upiAndBankingService.createNetBanking(netBankingRequest);
        return new ResponseEntity<NetBankingResponse>(response , HttpStatus.ACCEPTED);
    }


}
