package com.syc.finance.v1.bharat.telecom.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class RechargePlan {

    @Id
    private Long plan_id;
    private Long provider_id;
    private String plan_description;
    private double plan_price;
    private int valadity_days;

    @ManyToOne
    @JoinColumn(
            name = "plan_provider_id"

    )
    private PlanProviders planProvider;
}
