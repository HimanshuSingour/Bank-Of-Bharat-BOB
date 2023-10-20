package com.syc.finance.v1.bharat.telecom.service;

import com.syc.finance.v1.bharat.telecom.entity.PlanProviders;
import com.syc.finance.v1.bharat.telecom.entity.RechargePlan;
import com.syc.finance.v1.bharat.telecom.repository.PlanProvidersRepository;
import com.syc.finance.v1.bharat.telecom.repository.RechargePlanRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RechargeServiceImpl implements RechargeService{

    @Autowired
    PlanProvidersRepository planProvidersRepository;

    @Autowired
    RechargePlanRepository rechargePlanRepository;
    @Override
    public List<RechargePlan> getAllRechargePlansByProviderId() {
        return null;
    }

    @Override
    public void saveRechargePlan(RechargePlan rechargePlan) {
//        Optional<RechargePlan> isExistingRechargePlan = Optional.ofNullable(rechargePlanRepository.findByPlanId(rechargePlan.getPlan_id()));
        Optional<RechargePlan> isExistingRechargePlan = rechargePlanRepository.findById(rechargePlan.getPlan_id());
        if(!isExistingRechargePlan.isPresent()){
            rechargePlanRepository.save(rechargePlan);
        }
        else{
            //Throw some exception
            System.out.println("Plan alredy exist");
        }
    }

    @Override
    public void saveRechargePlans(List<RechargePlan> rechargePlans) {

    }

    @Override
    public void savePlanProvider(PlanProviders planProviders) {
//        Optional<PlanProviders> isExistingPlanProvider=Optional.ofNullable(planProvidersRepository.findByProviderId(planProviders.getProvider_id()));
        Optional<PlanProviders> isExistingPlanProvider=planProvidersRepository.findById(planProviders.getProvider_id());

        if(!isExistingPlanProvider.isPresent()){
            planProvidersRepository.save(planProviders);
        }
        else{
            //Throw some exception
            System.out.println("Provider alredy present");
        }
    }
}
