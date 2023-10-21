package com.syc.finance.v1.bharat.service.Telecom;

import com.syc.finance.v1.bharat.dto.Telecom.RechargePlanActivitionInfoRequest;
import com.syc.finance.v1.bharat.dto.Telecom.ServiceProviderRequest;
import com.syc.finance.v1.bharat.entity.telecom.RechargePlanes;
import com.syc.finance.v1.bharat.entity.telecom.ServiceProvider;
import com.syc.finance.v1.bharat.exceptions.DetailsNotFountException;
import com.syc.finance.v1.bharat.exceptions.ServiceProviderIsNullException;
import com.syc.finance.v1.bharat.exceptions.ServiceProviderValidationException;
import com.syc.finance.v1.bharat.repository.RechangeRepositories;
import com.syc.finance.v1.bharat.repository.ServiceProviderRepositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TelecomServiceImpl implements TelecomService {

    @Autowired
    private ServiceProviderRepositories serviceProviderRepositories;

    @Autowired
    private RechangeRepositories rechangeRepositories;


    // For Client (Add Provider Info)
    @Override
    public ServiceProvider addServiceProvides(ServiceProvider service) {

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
    public RechargePlanes addRechargePlan(RechargePlanes newRechargePlan) {

        return null;
    }

    @Override
    public List<RechargePlanes> getAllRechargePlansByProviderName(ServiceProviderRequest serviceProviderRequest) {
        return null;
    }

    @Override
    public List<RechargePlanes> getAllRechargeAboveTheGivenAmount(ServiceProviderRequest serviceProviderRequest, double aboveAmount) {
        return null;
    }

    @Override
    public List<RechargePlanes> getAllRechargeBelowTheGivenAmount(ServiceProviderRequest serviceProviderRequest, double BelowAmount) {
        return null;
    }

    @Override
    public RechargePlanActivitionInfoRequest getActivationInfo(ServiceProviderRequest serviceProviderRequest, String packId) {
        return null;
    }

}


