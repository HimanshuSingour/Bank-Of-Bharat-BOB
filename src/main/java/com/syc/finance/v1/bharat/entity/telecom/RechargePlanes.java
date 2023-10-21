package com.syc.finance.v1.bharat.entity.telecom;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "RECHARGE_PLANE")
public class RechargePlanes {


    @Id
    private String planeId;
    private String planName;
    private double planAmount;
    private int validityDays;
    private int dataLimitMB;
    private int voiceMinutes;
    private String planDescription;
    private String providerName;
    private boolean isInternational;
    private boolean isLimitedTimeOffer;
    private String planType;
    private String dataUsagePolicy;
    private String activationCode;
    private String coverageArea;
    private String specialNotes;
    private String additionalBenefits;


}
