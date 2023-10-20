package com.syc.finance.v1.bharat.entity.telecom;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "SERVICE_PROVIDER")
public class ServiceProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int serviceId;

    private String serviceProviderName;
    private String website;

    @OneToMany
    private RechargePlanes rechargePlanes;

}
