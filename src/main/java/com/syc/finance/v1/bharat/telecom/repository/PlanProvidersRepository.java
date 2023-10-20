package com.syc.finance.v1.bharat.telecom.repository;

import com.syc.finance.v1.bharat.telecom.entity.PlanProviders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanProvidersRepository extends JpaRepository< PlanProviders,Long> {

}
