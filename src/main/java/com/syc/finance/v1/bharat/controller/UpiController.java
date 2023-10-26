package com.syc.finance.v1.bharat.controller;


import com.syc.finance.v1.bharat.dto.UPI.GetUPIRequest;
import com.syc.finance.v1.bharat.dto.UPI.UPIRequest;
import com.syc.finance.v1.bharat.dto.UPI.UPIResponse;
import com.syc.finance.v1.bharat.service.UpiAndNetBanking.UPIAndNetBankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("finance/upi/v1/")
public class UpiController {

    @Autowired
    private UPIAndNetBankingService upiAndNetBankingService;

    @PostMapping("/upi-create")
    ResponseEntity<UPIResponse> upiCreateUp(@RequestBody UPIRequest upiRequest){
        UPIResponse response = upiAndNetBankingService.upiCreate(upiRequest);
        return new ResponseEntity<UPIResponse>(response , HttpStatus.ACCEPTED);
    }

    @GetMapping("/get-upi-details")
    ResponseEntity<UPIResponse> getUpiDetails(@RequestBody GetUPIRequest upiRequest) {
        UPIResponse response = upiAndNetBankingService.getYourUpiInInfo(upiRequest);
        return new ResponseEntity<UPIResponse>(response, HttpStatus.ACCEPTED);
    }

}
