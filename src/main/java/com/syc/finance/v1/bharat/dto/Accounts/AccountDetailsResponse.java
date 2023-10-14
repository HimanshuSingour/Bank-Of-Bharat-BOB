package com.syc.finance.v1.bharat.dto.Accounts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDetailsResponse {

    private String accountId;
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
    private String accountNumber;
    private String ifscCode;
    private String bankName;
    private String bankBranch;
    private String routingNumber;
    private String accountType;
    private double accountBalance;
    private String status;
    private LocalDateTime localDateTime;
    private LocalDate accountOpenDate;

    // adding NetBanking Info
    private String password;
    private String net_BANKING_ID;

    //Upi Details
    private String UPI_ID;
    private double UPI_BALANCE;



}
