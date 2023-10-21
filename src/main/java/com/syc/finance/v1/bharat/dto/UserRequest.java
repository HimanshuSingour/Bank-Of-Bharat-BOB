package com.syc.finance.v1.bharat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
        private String password;
        private String currentLocation;
        private String designation;
        private String country;
        private String accountType;



    }

