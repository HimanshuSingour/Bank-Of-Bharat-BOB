package com.syc.finance.v1.bharat.controller;
import com.syc.finance.v1.bharat.entity.telecom.RechargePlanes;
import com.syc.finance.v1.bharat.entity.telecom.ServiceProvider;
import com.syc.finance.v1.bharat.service.Telecom.TelecomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/telecom/v10/pro/v7")
public class TelecomController {

    @Autowired
    private TelecomService telecomService;


    @PostMapping("/save/provider")
    ResponseEntity<ServiceProvider> saveAll(@RequestBody ServiceProvider serviceProvider){
        ServiceProvider provider = telecomService.addServiceProvides(serviceProvider);
        return new ResponseEntity<>(provider , HttpStatus.CREATED);
    }

    @PostMapping("/save/recharge/plan/{planName}")
    ResponseEntity<RechargePlanes> saveAll( @RequestBody RechargePlanes newRechargePlan){
        RechargePlanes provider = telecomService.addRechargePlan(newRechargePlan);
        return new ResponseEntity<>(provider , HttpStatus.CREATED);
    }
}
