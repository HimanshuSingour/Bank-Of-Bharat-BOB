package com.syc.finance.v1.bharat.mapper;

import com.syc.finance.v1.bharat.dto.UserResponse;
import com.syc.finance.v1.bharat.entity.AccountInformation;

public class MapperToResponse {

//    Himashu

    public UserResponse userInformationToUserResponse(AccountInformation accountInformation) {

        UserResponse userResponse = new UserResponse();
        userResponse.setIsHaveUpiId(accountInformation.getIsHaveUpiId());
        userResponse.setAccountId(accountInformation.getAccountId());
        userResponse.setAccountHolderName(accountInformation.getAccountHolderName());
        userResponse.setAccountNumber(accountInformation.getAccountNumber());
        userResponse.setBankName(accountInformation.getBankName());
        userResponse.setBankBranch(accountInformation.getBankBranch());
        userResponse.setIfscCode(accountInformation.getIfscCode());
        userResponse.setAccountOpenDate(accountInformation.getAccountOpenDate());
        userResponse.setAccountBalance(accountInformation.getAccountBalance());
        userResponse.setRoutingNumber(accountInformation.getRoutingNumber());
        userResponse.setPassword(accountInformation.getPassword());
        userResponse.setAccountType(accountInformation.getAccountType());
        userResponse.setContactEmail(accountInformation.getContactEmail());
        userResponse.setContactPhone(accountInformation.getContactPhone());

        return userResponse;
    }

}
