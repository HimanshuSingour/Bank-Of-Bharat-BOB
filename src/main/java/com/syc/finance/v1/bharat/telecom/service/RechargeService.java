package com.syc.finance.v1.bharat.telecom.service;

import com.syc.finance.v1.bharat.telecom.entity.PlanProviders;
import com.syc.finance.v1.bharat.telecom.entity.RechargePlan;

import java.util.List;


public interface RechargeService {
    public List<RechargePlan> getAllRechargePlansByProviderId();
    public void saveRechargePlan(RechargePlan rechargePlan);
    public void saveRechargePlans(List<RechargePlan> rechargePlans);

    public void savePlanProvider(PlanProviders planProviders);


}
