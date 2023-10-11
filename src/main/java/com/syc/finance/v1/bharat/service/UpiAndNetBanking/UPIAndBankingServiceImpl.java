package com.syc.finance.v1.bharat.service.UpiAndNetBanking;

import com.syc.finance.v1.bharat.Utils.UPIDGenerater;
import com.syc.finance.v1.bharat.dto.UPI.UPIRequest;
import com.syc.finance.v1.bharat.dto.UPI.UPIResponse;
import com.syc.finance.v1.bharat.entity.AccountInformation;
import com.syc.finance.v1.bharat.exceptions.exceptionSteps.AccountNotFoundStep;
import com.syc.finance.v1.bharat.exceptions.exceptionSteps.UpiAlreadyExist;
import com.syc.finance.v1.bharat.repository.AccountDetailsRepositories;
import com.syc.finance.v1.bharat.repository.UPIDetailsRepositories;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.syc.finance.v1.bharat.Utils.AccountDetailsConstants.BANK_V3_UPI_CREATED;

@Service
@Slf4j
public class UPIAndBankingServiceImpl implements UPIAndBankingService {

    @Autowired
    private UPIDetailsRepositories upiDetailsRepositories;

    @Autowired
    private AccountDetailsRepositories accountDetailsRepositories;

    @Override
    public UPIResponse upiCreate(UPIRequest upiRequest) {


        AccountInformation accountInfo = accountDetailsRepositories.findByAccountIdAndIfscCode(upiRequest.getAccountNumber(),
                upiRequest.getIfscCode(), upiRequest.getBankPassword());


        if (accountInfo != null) {

            UPIDGenerater upidGenerater = new UPIDGenerater();
            String isUpiPresentInAccount = accountInfo.getIsHaveUpiId();

            String UPI_GENERATED_ID = UUID.randomUUID().toString();
            String UPI_ID = upidGenerater.generateUpiId(accountInfo.getAccountHolderName());
            String UPI_CODE = upidGenerater.generatePin();

            if ("NO".equals(isUpiPresentInAccount)) {

                UPIResponse response = new UPIResponse();
                response.setAccountNumber(accountInfo.getAccountNumber());
                response.setContactNumber(accountInfo.getContactPhone());
                response.setContactEmail(accountInfo.getContactEmail());
                response.setUPI_GENERATED_ID(UPI_GENERATED_ID);
                response.setUPI_ID(UPI_ID);
                response.setUPI_CODE(UPI_CODE);
                response.setUPI_BALANCE(0.0);
                response.setResponseMessage(BANK_V3_UPI_CREATED);

//                upiDetailsRepositories.save(response); need to fix yess

                accountInfo.setIsHaveUpiId("YES");
                accountDetailsRepositories.save(accountInfo);
                return response;

            } else {

                throw new UpiAlreadyExist("");
            }
        }
        throw new AccountNotFoundStep("");
    }
}
