package com.syc.finance.v1.bharat.service.Telecom;

import com.syc.finance.v1.bharat.dto.Telecom.ServiceProviderRequest;
import com.syc.finance.v1.bharat.entity.telecom.RechargePlanes;
import com.syc.finance.v1.bharat.entity.telecom.ServiceProvider;
import com.syc.finance.v1.bharat.exceptions.RechangeDetailsNotValid;
import com.syc.finance.v1.bharat.exceptions.ServiceProviderIsNullException;
import com.syc.finance.v1.bharat.exceptions.ServiceProviderValidationException;
import com.syc.finance.v1.bharat.repository.RechangeRepositories;
import com.syc.finance.v1.bharat.repository.ServiceProviderRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
public class TelecomServiceImpl implements TelecomService {

    @Autowired
    private ServiceProviderRepositories serviceProviderRepositories;

    @Autowired
    private RechangeRepositories rechangeRepositories;



    // For Client (Add Provider Info)
    @Override
    public ServiceProvider addServiceProvide(ServiceProvider service) {

        if (service == null) {
            throw new ServiceProviderIsNullException("Service provider is null");
        }

        if (service.getServiceProviderName() == null || service.getServiceProviderName().isEmpty()) {
            throw new ServiceProviderValidationException("Service provider name is required");
        }

        ServiceProvider provider = ServiceProvider.builder()
                .providerId(service.getProviderId())
                .serviceProviderName(service.getServiceProviderName())
                .website(service.getWebsite())
                .build();

        serviceProviderRepositories.save(provider);

        return provider;
    }

    @Override
    public RechargePlanes addRechargePlans(RechargePlanes rechargePlanes, ServiceProviderRequest serviceProviderRequest) {

         ServiceProvider provider = serviceProviderRepositories.findByServiceProviderName(serviceProviderRequest.getServiceProviderName());

       if(provider != null){
           System.out.println("provider is present");
       }
       else {
           System.out.println("Not");
       }

        return rechargePlanes;
    }

}
