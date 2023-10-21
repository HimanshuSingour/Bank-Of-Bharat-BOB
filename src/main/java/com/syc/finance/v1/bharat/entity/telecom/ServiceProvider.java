package com.syc.finance.v1.bharat.entity.telecom;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "SERVICE_PROVIDER")
public class ServiceProvider {

    @Id
    private String providerId;

    private String serviceProviderName;
    private String website;

    @OneToMany(targetEntity = RechargePlanes.class , cascade = CascadeType.ALL)
    @JoinColumn(name = "provider_id" , referencedColumnName = "providerId")
    private List<RechargePlanes> planes;

}
