package com.syc.finance.v1.bharat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.NotFound;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {


    private String accountHolderName;
    private String contactEmail;
    private String contactPhone;
    private String gender;
    private String contactAddress;
    private String stateOfOrigin;
    private String pinCodeNumber;
    private String currentLocation;
    private String designation;
    private String country;
    private String accountType;

}
