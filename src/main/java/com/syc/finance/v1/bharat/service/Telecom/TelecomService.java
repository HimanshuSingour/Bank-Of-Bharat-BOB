package com.syc.finance.v1.bharat.service.Telecom;

import com.syc.finance.v1.bharat.dto.Telecom.ServiceProviderRequest;
import com.syc.finance.v1.bharat.entity.telecom.RechargePlanes;
import com.syc.finance.v1.bharat.entity.telecom.ServiceProvider;

public interface TelecomService {

    // for client

    ServiceProvider addServiceProvide(ServiceProvider serviceProvider);
    RechargePlanes addRechargePlans(RechargePlanes rechargePlanes, ServiceProviderRequest serviceProviderRequest);

}
