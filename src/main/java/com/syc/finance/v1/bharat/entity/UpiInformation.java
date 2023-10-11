package com.syc.finance.v1.bharat.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "UPI_INFO")
public class UpiInformation {

    @Id
    private String globalId;
    private String accountNumber;
    private String ifscCode;
    private String bankPassword;
    private String contactNumber;
    private String contactEmail;
    private String UPI_GENERATED_ID;
    private String UPI_ID;
    private String UPI_CODE;
    private double UPI_BALANCE;
    private String responseMessage;
}
