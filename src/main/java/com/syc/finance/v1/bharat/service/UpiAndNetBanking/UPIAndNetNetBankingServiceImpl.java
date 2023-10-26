package com.syc.finance.v1.bharat.service.UpiAndNetBanking;

import com.syc.finance.v1.bharat.notification.NotificationsUtility;
import com.syc.finance.v1.bharat.utils.InternetBankingIdGenerator;
import com.syc.finance.v1.bharat.utils.UPIDGenerater;
import com.syc.finance.v1.bharat.dto.InternetBanking.GetNetBankingRequest;
import com.syc.finance.v1.bharat.dto.InternetBanking.NetBankingRequest;
import com.syc.finance.v1.bharat.dto.InternetBanking.NetBankingResponse;
import com.syc.finance.v1.bharat.dto.TransferMoney.TransferMoneyRequest;
import com.syc.finance.v1.bharat.dto.TransferMoney.TransferMoneyResponse;
import com.syc.finance.v1.bharat.dto.UPI.GetUPIRequest;
import com.syc.finance.v1.bharat.dto.UPI.UPIRequest;
import com.syc.finance.v1.bharat.dto.UPI.UPIResponse;
import com.syc.finance.v1.bharat.entity.AccountInformation;
import com.syc.finance.v1.bharat.entity.NetBankingInformation;
import com.syc.finance.v1.bharat.entity.UpiInformation;
import com.syc.finance.v1.bharat.exceptions.*;
import com.syc.finance.v1.bharat.repository.AccountDetailsRepositories;
import com.syc.finance.v1.bharat.repository.NetBankingRepositories;
import com.syc.finance.v1.bharat.repository.UPIDetailsRepositories;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.syc.finance.v1.bharat.constants.AccountDetailsConstants.*;


@Service
@Slf4j
public class UPIAndNetNetBankingServiceImpl implements UPIAndNetBankingService {

    @Autowired
    private UPIDetailsRepositories upiDetailsRepositories;

    @Autowired
    private AccountDetailsRepositories accountDetailsRepositories;

    @Autowired
    private NetBankingRepositories netBankingRepositories;

    @Autowired
    private NotificationsUtility notificationsUtility;

    @Override
    public UPIResponse upiCreate(UPIRequest upiRequest) {

        AccountInformation accountInfo = accountDetailsRepositories.findByAccountIdAndIfscCode(upiRequest.getAccountNumber(),
                upiRequest.getIfscCode(), upiRequest.getPassword());

        NetBankingInformation existingNetBanking = netBankingRepositories.findByAccountIdAndIfscCode(
                upiRequest.getAccountNumber(),
                upiRequest.getIfscCode());

        if (accountInfo != null) {

            if (existingNetBanking == null) {

                notificationsUtility.sendForNetBankingCreationAlert();
                throw new NotHavingNetbanking("Account need to create a net banking ID first in order to be able to create a UPI ID.");

            } else {

                UPIDGenerater upidGenerater = new UPIDGenerater();
                String isUpiPresentInAccount = accountInfo.getIsHaveUpiId();

                String UPI_GENERATED_ID = UUID.randomUUID().toString();
                String UPI_ID = upidGenerater.generateUpiId(accountInfo.getAccountHolderName());
                String UPI_CODE = upidGenerater.generatePin();

                if (BANK_V3_NOTA_UPI_ID.equals(isUpiPresentInAccount)) {

                    UPIResponse response = new UPIResponse();
                    response.setAccountNumber(accountInfo.getAccountNumber());
                    response.setContactNumber(accountInfo.getContactPhone());
                    response.setContactEmail(accountInfo.getContactEmail());
                    response.setUpiId(UPI_ID);
                    response.setUpiId(UPI_CODE);
                    response.setUPI_BALANCE(0.0);
                    response.setResponseMessage(BANK_V3_UPI_CREATED);

                    UpiInformation upiInformation = new UpiInformation();
                    upiInformation.setGlobalId(UPI_GENERATED_ID);
                    upiInformation.setAccountNumber(accountInfo.getAccountNumber());
                    upiInformation.setIfscCode(accountInfo.getIfscCode());
                    upiInformation.setBankPassword(accountInfo.getPassword());
                    upiInformation.setContactNumber(accountInfo.getContactPhone());
                    upiInformation.setContactEmail(accountInfo.getContactEmail());
                    upiInformation.setUpiId(UPI_ID);
                    upiInformation.setUPI_CODE(UPI_CODE);
                    upiInformation.setUPI_BALANCE(0.0);
                    upiInformation.setResponseMessage(BANK_V3_UPI_CREATED);

                    upiDetailsRepositories.save(upiInformation);

                    accountInfo.setIsHaveUpiId("YES");
                    accountDetailsRepositories.save(accountInfo);

                    notificationsUtility.sendForUPIIdCreation(response.getUpiId());

                    return response;

                } else {

                    throw new UpiAlreadyExist("Your account number and IFSC code are already linked to a UPI ID");
                }
            }
        }
        throw new AccountNotFoundStep("The details you have entered are incorrect. There is no account with these details. Please double-check the information and try again.");
    }

    @Override
    public NetBankingResponse createNetBanking(NetBankingRequest netBankingRequest) {

        AccountInformation accountInformation = accountDetailsRepositories.findByAccountIdAndIfscCode(
                netBankingRequest.getAccountNumber(),
                netBankingRequest.getIfscCode(),
                netBankingRequest.getPassword()
        );

        if(accountInformation != null){

            NetBankingResponse netBankingResponse = new NetBankingResponse();

            NetBankingInformation existingNetBanking = netBankingRepositories.findByAccountIdAndIfscCode(
                    netBankingRequest.getAccountNumber(),
                    netBankingRequest.getIfscCode());

            if (existingNetBanking == null) {

                InternetBankingIdGenerator internetBankingIdGenerator = new InternetBankingIdGenerator();
                NetBankingInformation netBankingResponseEntity = new NetBankingInformation();

                netBankingResponse.setNetId(UUID.randomUUID().toString());
                netBankingResponse.setAccountHolderName(accountInformation.getAccountHolderName());
                netBankingResponse.setAccountNumber(accountInformation.getAccountNumber());
                netBankingResponse.setIfscCode(accountInformation.getIfscCode());
                netBankingResponse.setLocalDateTime(LocalDateTime.now());
                netBankingResponse.setNet_BANKING_ID(internetBankingIdGenerator.generateInternetBankingId());

                netBankingResponseEntity.setNetId(netBankingResponse.getNetId());
                netBankingResponseEntity.setAccountHolderName(netBankingResponse.getAccountHolderName());
                netBankingResponseEntity.setAccountNumber(netBankingResponse.getAccountNumber());
                netBankingResponseEntity.setIfscCode(netBankingResponse.getIfscCode());
                netBankingResponseEntity.setLocalDateTime(netBankingResponse.getLocalDateTime());
                netBankingResponseEntity.setNet_BANKING_ID(netBankingResponse.getNet_BANKING_ID());
                netBankingResponseEntity.setPassword(netBankingRequest.getPassword());

                notificationsUtility.sendForNetBankingCreation();

                netBankingRepositories.save(netBankingResponseEntity);
                return netBankingResponse;

            } else {
                throw new NetBankingIdAlreadyExist("You already have a bank ID. If you've forgotten it, please contact our support team..");
            }
        }

       throw new AccountNotFoundStep("The details you have entered are incorrect. There is no account with these details. Please double-check the information and try again.");
    }

    @Override
    public UPIResponse getYourUpiInInfo(GetUPIRequest upiRequest) {

        UpiInformation upiInformation = upiDetailsRepositories.findByAccountNumberAndPassword(upiRequest.getAccountNumber()
        , upiRequest.getBankPassword());

        if(upiInformation != null){

            UPIResponse response = new UPIResponse();
            response.setAccountNumber(upiInformation.getAccountNumber());
            response.setContactNumber(upiInformation.getContactNumber());
            response.setContactEmail(upiInformation.getContactEmail());
            response.setUpiId(upiInformation.getUpiId());
            response.setUPI_CODE(upiInformation.getUPI_CODE());
            response.setUPI_BALANCE(upiInformation.getUPI_BALANCE());
            response.setResponseMessage(upiInformation.getResponseMessage());
            return response;
        }
        throw new DetailsNotFountException("Details Not Found..");
    }

    @Override
    public NetBankingResponse getYourNetBankingInfo(GetNetBankingRequest netBankingRequest) {

        NetBankingInformation netBankingInformation = netBankingRepositories.findByAccountIdAndIfscCode(netBankingRequest.getAccountNumber()
                , netBankingRequest.getIfscCode());

        if(netBankingInformation != null){

            NetBankingResponse response = new NetBankingResponse();
            response.setNetId(netBankingInformation.getNetId());
            response.setAccountHolderName(netBankingInformation.getAccountHolderName());
            response.setAccountNumber(netBankingInformation.getAccountNumber());
            response.setIfscCode(netBankingInformation.getIfscCode());
            response.setLocalDateTime(netBankingInformation.getLocalDateTime());
            response.setNet_BANKING_ID(netBankingInformation.getNet_BANKING_ID());
            return response;
        }

        throw new DetailsNotFountException("Details Not Found..");
    }

    @Override
    public TransferMoneyResponse moneyTransferAccountToAccount(TransferMoneyRequest transferMoneyRequest) {

        AccountInformation accountInformationForRecipient = accountDetailsRepositories.findByAccountNumberAndName(
                transferMoneyRequest.getFullNameOfRecipient(),
                transferMoneyRequest.getAccountNumberOfRecipient());

        AccountInformation accountInformationForSender = accountDetailsRepositories.findByAccountNumber(
                transferMoneyRequest.getAccountNumberOfSender());

        if(accountInformationForRecipient != null && accountInformationForSender != null){

            //senders money
            double sendersMoney = transferMoneyRequest.getTransferAmount();

            if(sendersMoney >= 10000){

                // need to pass the phoneNumber in below methods
                notificationsUtility.sendForHighAmountOfMoneyTransfer();

                double sendersBankAccount = accountInformationForSender.getAccountBalance() - sendersMoney;

                accountInformationForSender.setAccountBalance(sendersBankAccount);
                accountDetailsRepositories.save(accountInformationForSender);

                //recipients money
                double recipientMoney = accountInformationForRecipient.getAccountBalance();
                double recdBankAccount = sendersMoney + recipientMoney;

                accountInformationForRecipient.setAccountBalance(recdBankAccount);
                accountDetailsRepositories.save(accountInformationForRecipient);

                //response
                TransferMoneyResponse transferMoneyResponse = new TransferMoneyResponse();
                transferMoneyResponse.setResponseMessage(MONEY_SEND_SUCCESSFULLY);
                return transferMoneyResponse;

            }
            else{

                double sendersBankAccount = accountInformationForSender.getAccountBalance() - sendersMoney;

                accountInformationForSender.setAccountBalance(sendersBankAccount);
                accountDetailsRepositories.save(accountInformationForSender);

                //recipients money
                double recipientMoney = accountInformationForRecipient.getAccountBalance();
                double recdBankAccount = sendersMoney + recipientMoney;

                accountInformationForRecipient.setAccountBalance(recdBankAccount);
                accountDetailsRepositories.save(accountInformationForRecipient);

                //response
                TransferMoneyResponse transferMoneyResponse = new TransferMoneyResponse();
                transferMoneyResponse.setResponseMessage(MONEY_SEND_SUCCESSFULLY);
                return transferMoneyResponse;
            }

        }

        else {

            throw new DetailsNotFountException("The details you have entered are incorrect. There is no account with these details. Please double-check the information and try again.");
        }

    }
}

