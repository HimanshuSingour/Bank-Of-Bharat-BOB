package com.syc.finance.v1.bharat.entity.telecom;

import lombok.Data;

@Data
public class RechargePlanes {


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
