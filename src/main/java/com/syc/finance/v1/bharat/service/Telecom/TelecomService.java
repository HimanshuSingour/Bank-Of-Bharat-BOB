package com.syc.finance.v1.bharat.service.Telecom;
import com.syc.finance.v1.bharat.dto.Telecom.RechargePlanActivitionInfoRequest;
import com.syc.finance.v1.bharat.dto.Telecom.ServiceProviderRequest;
import com.syc.finance.v1.bharat.entity.telecom.RechargePlanes;
import com.syc.finance.v1.bharat.entity.telecom.ServiceProvider;

import java.util.List;

public interface TelecomService {

    // for Developers
    ServiceProvider addServiceProvides(ServiceProvider serviceProvider);
    RechargePlanes addRechargePlan(RechargePlanes newRechargePlan);

    // Users
    List<RechargePlanes> getAllRechargePlansByProviderName(ServiceProviderRequest serviceProviderRequest);
    List<RechargePlanes> getAllRechargeAboveTheGivenAmount(ServiceProviderRequest serviceProviderRequest , double aboveAmount);
    List<RechargePlanes> getAllRechargeBelowTheGivenAmount(ServiceProviderRequest serviceProviderRequest , double BelowAmount);
    RechargePlanActivitionInfoRequest getActivationInfo(ServiceProviderRequest serviceProviderRequest , String packId);








}
