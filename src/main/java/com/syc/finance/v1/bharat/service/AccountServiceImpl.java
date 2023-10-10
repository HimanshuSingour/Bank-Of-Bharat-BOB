package com.syc.finance.v1.bharat.service;
import com.syc.finance.v1.bharat.Mapper.MapperToResponse;
import com.syc.finance.v1.bharat.Mapper.MapperToUpdateResponse;
import com.syc.finance.v1.bharat.Notifications.NotificationBalanceEnquiry;
import com.syc.finance.v1.bharat.Notifications.NotificationForCredit;
import com.syc.finance.v1.bharat.Notifications.NotificationForDebited;
import com.syc.finance.v1.bharat.Utils.AccountDeletedSuccessResponse;
import com.syc.finance.v1.bharat.Utils.AccountDetailsGenarators;
import com.syc.finance.v1.bharat.dto.*;
import com.syc.finance.v1.bharat.dto.Accounts.*;
import com.syc.finance.v1.bharat.dto.BalanceEnquiry.BalanceEnquireyRequest;
import com.syc.finance.v1.bharat.dto.BalanceEnquiry.BalanceEnquiryResponse;
import com.syc.finance.v1.bharat.dto.Credit.CreditCredential;
import com.syc.finance.v1.bharat.dto.Credit.CreditResponse;
import com.syc.finance.v1.bharat.dto.Debit.DebitCredential;
import com.syc.finance.v1.bharat.dto.Debit.DebitedResponse;
import com.syc.finance.v1.bharat.dto.Transaction.TransactionRequest;
import com.syc.finance.v1.bharat.entity.AccountInformation;
import com.syc.finance.v1.bharat.exceptions.exceptionSteps.AccountBalanceMinimumSteps;
import com.syc.finance.v1.bharat.exceptions.exceptionSteps.AccountNotFoundStep;
import com.syc.finance.v1.bharat.exceptions.exceptionSteps.EmailAlreadyExistStep;
import com.syc.finance.v1.bharat.repository.TransactionHistoryRepository;
import com.syc.finance.v1.bharat.repository.AccountDetailsRepositories;
import com.twilio.Twilio;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.syc.finance.v1.bharat.Utils.AccountDetailsConstants.*;


@Service
@Slf4j
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDetailsRepositories accountDetailsRepositories;

    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;

    @Autowired
    private TransactionService transactionService;

    static {
        Twilio.init(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN);
    }

    @Override
    public UserResponse createAccount(UserRequest userRequest) {

        String userIdGenerated = UUID.randomUUID().toString();
        MapperToResponse mapperToResponse = new MapperToResponse();

        Optional<AccountInformation> existingEmail = Optional.ofNullable(accountDetailsRepositories.findByContactEmail(userRequest.getContactEmail()));
        Optional<AccountInformation> existingPhone = Optional.ofNullable(accountDetailsRepositories.findByContactPhoneNumber(userRequest.getContactPhone()));

        if (existingEmail.isPresent() || existingPhone.isPresent()) {
            throw new EmailAlreadyExistStep("");
        }
        else{

            AccountDetailsGenarators accountDetailsGenarators = new AccountDetailsGenarators();
            String IFSC_CODE = accountDetailsGenarators.gereratedIFSC();
            String BANK_PIN = accountDetailsGenarators.generateBankCode();

            AccountInformation accountInformation = AccountInformation.builder()
                    .accountId(userIdGenerated)
                    .accountHolderName(userRequest.getAccountHolderName())
                    .contactEmail(userRequest.getContactEmail())
                    .contactPhone(userRequest.getContactPhone())
                    .gender(userRequest.getGender())
                    .contactAddress(userRequest.getContactAddress())
                    .stateOfOrigin(userRequest.getStateOfOrigin())
                    .pinCodeNumber(userRequest.getPinCodeNumber())
                    .currentLocation(userRequest.getCurrentLocation())
                    .designation(userRequest.getDesignation())
                    .country(userRequest.getCountry())
                    .bankName(BANK_VI_NAME)
                    .bankBranch(BANK_VI_BRANCH)
                    .routingNumber(BANK_VI_ROUTING)
                    .accountType(userRequest.getAccountType())
                    .status(BANK_VI_STATUS)
                    .localDateTime(LocalDateTime.now())
                    .accountOpenDate(LocalDate.now())
                    .accountNumber(accountDetailsGenarators.generateAccountNumber())
                    .ifscCode("BOBI" + IFSC_CODE)
                    .bankPinCode(BANK_PIN)
                    .build();

            accountDetailsRepositories.save(accountInformation);

            UserResponse userRes = mapperToResponse.userInformationToUserResponse(accountInformation);
            userRes.setMessage(BANK_VI_ACCOUNT_CREATED);
            return userRes;

        }

    }


    @Override
    public AccountUpdateDetailsResponse updateAccountDetails(AccountUpdatingDetailsRequest accountUpdatingDetailsRequest) {

        Optional<AccountInformation> userInformation = Optional.ofNullable(accountDetailsRepositories.findByAccountNumber(
                accountUpdatingDetailsRequest.getAccountNumber()));

        if(userInformation.isPresent()){

            AccountInformation updateAccountDetails = AccountInformation.builder()
                    .accountHolderName(accountUpdatingDetailsRequest.getAccountHolderName())
                    .contactPhone(accountUpdatingDetailsRequest.getContactPhone())
                    .contactEmail(accountUpdatingDetailsRequest.getContactEmail())
                    .accountType(accountUpdatingDetailsRequest.getAccountType())
                    .contactAddress(accountUpdatingDetailsRequest.getContactAddress())
                    .stateOfOrigin(accountUpdatingDetailsRequest.getStateOfOrigin())
                    .pinCodeNumber(accountUpdatingDetailsRequest.getPinCodeNumber())
                    .currentLocation(accountUpdatingDetailsRequest.getCurrentLocation())
                    .designation(accountUpdatingDetailsRequest.getDesignation())
                    .country(accountUpdatingDetailsRequest.getCountry())
                    .accountType(accountUpdatingDetailsRequest.getAccountType())
                    .localDateTime(LocalDateTime.now())
                    .build();


            MapperToUpdateResponse mapperToUpdateResponse = new MapperToUpdateResponse();

            return mapperToUpdateResponse.userInformationToUpdateAccountResponse(updateAccountDetails);

        }

        else
            throw new AccountNotFoundStep("");
    }


    @Override
    public AccountDeletedSuccessResponse deleteAccount(AccountDeleteAccountDetailsRequest accountDetailsRequest) {

        AccountInformation accountInformation = accountDetailsRepositories.findByAccountIdAndIfscCode(
                accountDetailsRequest.getAccountNumber(),
                accountDetailsRequest.getContactEmail()
        );

        if (accountInformation != null) {
            accountDetailsRepositories.delete(accountInformation);
            return new AccountDeletedSuccessResponse("Account deleted successfully.");
        } else {
            throw new AccountNotFoundStep("Account not found.");
        }
    }


    @Override
    public AccountDetailsResponse getYourAccountDetails(String accountNumber , String IFSCCode) {

        AccountInformation accountInformation = accountDetailsRepositories.findByAccountIdAndIfscCode(accountNumber , IFSCCode);

        AccountDetailsResponse accountDetailsResponse = new AccountDetailsResponse();

        if (accountInformation != null){

                accountDetailsResponse.setAccountId(accountInformation.getAccountId());
                accountDetailsResponse.setAccountHolderName(accountInformation.getAccountHolderName());
                accountDetailsResponse.setContactEmail(accountInformation.getContactEmail());
                accountDetailsResponse.setContactPhone(accountInformation.getContactPhone());
                accountDetailsResponse.setGender(accountInformation.getGender());
                accountDetailsResponse.setContactAddress(accountInformation.getContactAddress());
                accountDetailsResponse.setStateOfOrigin(accountInformation.getStateOfOrigin());
                accountDetailsResponse.setPinCodeNumber(accountInformation.getPinCodeNumber());
                accountDetailsResponse.setCurrentLocation(accountInformation.getCurrentLocation());
                accountDetailsResponse.setDesignation(accountInformation.getDesignation());
                accountDetailsResponse.setCountry(accountInformation.getCountry());
                accountDetailsResponse.setAccountNumber(accountInformation.getAccountNumber());
                accountDetailsResponse.setIfscCode(accountInformation.getIfscCode());
                accountDetailsResponse.setBankName(accountInformation.getBankName());
                accountDetailsResponse.setBankBranch(accountInformation.getBankBranch());
                accountDetailsResponse.setRoutingNumber(accountInformation.getRoutingNumber());
                accountDetailsResponse.setAccountType(accountInformation.getAccountType());
                accountDetailsResponse.setAccountBalance(accountInformation.getAccountBalance());
                accountDetailsResponse.setStatus(accountInformation.getStatus());
                accountDetailsResponse.setLocalDateTime(LocalDateTime.now());
                accountDetailsResponse.setAccountOpenDate(LocalDate.now());
                return accountDetailsResponse;
        }
        else{
            throw new AccountNotFoundStep("");
        }
    }

    @Override
    public CreditResponse creditYourMoney(CreditCredential creditCredential) {

        AccountInformation accountInformation = accountDetailsRepositories.findByAccountIdAndIfscCode(creditCredential.getAccountNumber(),
                creditCredential.getIfscCode());

        if(accountInformation != null){

            double creditedAmount = creditCredential.getCreditYourMoney();
            double currentBalance = accountInformation.getAccountBalance() + creditedAmount;

            accountInformation.setAccountBalance(currentBalance);

            accountInformation = accountDetailsRepositories.save(accountInformation);
            creditCredential.setCreditYourMoney(accountInformation.getAccountBalance());
            String autoGeneratedId = UUID.randomUUID().toString();

            NotificationForCredit notificationForCredit = new NotificationForCredit();

            notificationForCredit.sendForDebitedAccount(accountInformation.getAccountNumber(),
                    accountInformation.getAccountBalance() , accountInformation.getContactPhone());

            CreditResponse addingMoney = new CreditResponse();

            TransactionRequest transactionRequest = new TransactionRequest();
            transactionRequest.setTransactionId(autoGeneratedId);
            transactionRequest.setDebitedOrCredited(BANK_V2_CREDIT);
            transactionRequest.setAccountNumber(accountInformation.getAccountNumber());
            transactionRequest.setDebitOrCreditMoney(creditedAmount);
            transactionRequest.setLocalDateTime(LocalDateTime.now());

            transactionService.saveTransaction(transactionRequest);
            addingMoney.setCreditId(autoGeneratedId);
            addingMoney.setAccountHolderName(accountInformation.getAccountHolderName());
            addingMoney.setAccountNumber(accountInformation.getAccountNumber());
            addingMoney.setIfscCode(accountInformation.getIfscCode());
            addingMoney.setBankName(accountInformation.getBankName());
            addingMoney.setStatusMoney(BANK_VI_ACCOUNT_BALANCE_CREDIT);
            addingMoney.setLocalDateTime(LocalDateTime.now());
            addingMoney.setCurrentBalance(currentBalance);
            return addingMoney;
        }

        else throw new AccountNotFoundStep("");

    }

    @Override
    public DebitedResponse debitYourMoney(DebitCredential debitCredential) {

        AccountInformation accountInformation = accountDetailsRepositories.findByAccountIdAndIfscCode(debitCredential.getAccountNumber(),
                debitCredential.getIfscCode());

        if(accountInformation != null){

            double debitedAmount = debitCredential.getDebitYourMoney();
            double currentBalance = accountInformation.getAccountBalance() - debitedAmount;

            if(currentBalance >= debitedAmount){

                accountInformation.setAccountBalance(currentBalance);
                accountInformation = accountDetailsRepositories.save(accountInformation);
                debitCredential.setDebitYourMoney(accountInformation.getAccountBalance());
                String autoGeneratedId = UUID.randomUUID().toString();

                NotificationForDebited notificationForDebited = new NotificationForDebited();

                notificationForDebited.sendForCredit(accountInformation.getAccountNumber(),
                        accountInformation.getAccountBalance() , accountInformation.getContactPhone());

                DebitedResponse miniMoney = new DebitedResponse();

                TransactionRequest transactionRequest = new TransactionRequest();
                transactionRequest.setTransactionId(autoGeneratedId);
                transactionRequest.setDebitedOrCredited(BANK_V2_DEBIT);
                transactionRequest.setLocalDateTime(LocalDateTime.now());
                transactionRequest.setAccountNumber(accountInformation.getAccountNumber());
                transactionRequest.setDebitOrCreditMoney(debitedAmount);

                transactionService.saveTransaction(transactionRequest);

                miniMoney.setDebitedId(autoGeneratedId);
                miniMoney.setAccountHolderName(accountInformation.getAccountHolderName());
                miniMoney.setAccountNumber(accountInformation.getAccountNumber());
                miniMoney.setIfscCode(accountInformation.getIfscCode());
                miniMoney.setStatusDebit(BANK_VI_ACCOUNT_BALANCE_DEBITED);
                miniMoney.setLocalDateTime(LocalDateTime.now());
                miniMoney.setCurrentBalance(currentBalance);
                miniMoney.setDebitYourMoney(debitedAmount);

                return miniMoney;
            }
            else {
                throw new AccountBalanceMinimumSteps("");
            }
        }
        else throw new AccountNotFoundStep("");
    }


    @Override
    public BalanceEnquiryResponse balanceEnquiry(BalanceEnquireyRequest balanceEnquireyRequest) {

        AccountInformation accountInformation = accountDetailsRepositories.findByAccountIdAndIfscCode(balanceEnquireyRequest.getAccountNumber(),
                balanceEnquireyRequest.getIfscCode());

        NotificationBalanceEnquiry notificationBalanceEnquiry = new NotificationBalanceEnquiry();

        notificationBalanceEnquiry.sendForBalanceEnquiry(accountInformation.getAccountNumber(),
                accountInformation.getAccountBalance() , accountInformation.getContactPhone());

        String balanceId = UUID.randomUUID().toString();
        BalanceEnquiryResponse response = new BalanceEnquiryResponse();
        response.setBalanceId(balanceId);
        response.setAccountNumber(accountInformation.getAccountNumber());
        response.setStatusMessage(BANK_VI_ACCOUNT_BALANCE_STATUS);
        response.setYourBalance(accountInformation.getAccountBalance());
        response.setLocalDateTime(LocalDateTime.now());
        return response;
    }
}
