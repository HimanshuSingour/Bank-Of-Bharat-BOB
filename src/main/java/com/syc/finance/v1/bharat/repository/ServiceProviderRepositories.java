package com.syc.finance.v1.bharat.repository;

import com.syc.finance.v1.bharat.entity.telecom.RechargePlanes;
import com.syc.finance.v1.bharat.entity.telecom.ServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceProviderRepositories extends JpaRepository<ServiceProvider, Integer> {

    @Query("SELECT s FROM ServiceProvider s WHERE s.serviceProviderName = :serviceProviderName")
    ServiceProvider findByServiceProviderName(String serviceProviderName);


}
