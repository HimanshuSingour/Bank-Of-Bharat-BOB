package com.syc.finance.v1.bharat.telecom.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor@Builder

public class PlanProviders {

    @Id
    private long provider_id;
    private String provider_name;
    @OneToMany(mappedBy = "planProvider")
    private List<RechargePlan> plans;
}
