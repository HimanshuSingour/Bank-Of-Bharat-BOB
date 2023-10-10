package com.syc.finance.v1.bharat.MyConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ConnectToServices {

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
