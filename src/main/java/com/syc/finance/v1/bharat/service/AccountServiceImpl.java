package com.syc.finance.v1.bharat.service;

import com.syc.finance.v1.bharat.dto.Update.UpdateAmountManually;
import com.syc.finance.v1.bharat.dto.Update.UpdateAmountResponse;
import com.syc.finance.v1.bharat.mapper.MapperToResponse;
import com.syc.finance.v1.bharat.mapper.MapperToUpdateResponse;
import com.syc.finance.v1.bharat.notification.*;
import com.syc.finance.v1.bharat.utils.AccountDeletedSuccessResponse;
import com.syc.finance.v1.bharat.utils.AccountDetailsGenarators;
import com.syc.finance.v1.bharat.utils.AccountlLimitReached;
import com.syc.finance.v1.bharat.dto.*;
import com.syc.finance.v1.bharat.dto.Accounts.*;
import com.syc.finance.v1.bharat.dto.BalanceEnquiry.BalanceEnquireyRequest;
import com.syc.finance.v1.bharat.dto.BalanceEnquiry.BalanceEnquiryResponse;
import com.syc.finance.v1.bharat.dto.Credit.CreditCredential;
import com.syc.finance.v1.bharat.dto.Credit.CreditResponse;
import com.syc.finance.v1.bharat.dto.Debit.DebitCredential;
import com.syc.finance.v1.bharat.dto.Debit.DebitedResponse;
import com.syc.finance.v1.bharat.dto.Transaction.TransactionRequest;
import com.syc.finance.v1.bharat.dto.UPIPay.AddMoneyToUPIFromAccountRequest;
import com.syc.finance.v1.bharat.dto.UPIPay.AddMoneyToUPIFromAccountResponse;
import com.syc.finance.v1.bharat.dto.UPIPay.AddMoneyFromAccountToUPIRequest;
import com.syc.finance.v1.bharat.dto.UPIPay.AddMoneyFromAccountToUPIResponse;
import com.syc.finance.v1.bharat.entity.AccountInformation;
import com.syc.finance.v1.bharat.entity.NetBankingInformation;

import static com.syc.finance.v1.bharat.constants.AccountDetailsConstants.*;

import com.syc.finance.v1.bharat.entity.UpiInformation;
import com.syc.finance.v1.bharat.exceptions.*;
import com.syc.finance.v1.bharat.repository.TransactionHistoryRepository;
import com.syc.finance.v1.bharat.repository.AccountDetailsRepositories;
import com.syc.finance.v1.bharat.repository.UPIDetailsRepositories;
import com.syc.finance.v1.bharat.service.TransactionService.TransactionService;
import com.twilio.Twilio;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDetailsRepositories accountDetailsRepositories;
    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;
    @Autowired
    private UPIDetailsRepositories upiDetailsRepositories;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountlLimitReached accountlLimitReached;
    @Autowired
    private NotificationsUtility notificationsUtility;


    static {
        Twilio.init(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN);
    }

    @Override
    public UserResponse createAccount(UserRequest userRequest) {

        log.info("Creating a new account.");

        String userIdGenerated = UUID.randomUUID().toString();
        MapperToResponse mapperToResponse = new MapperToResponse();

        Optional<AccountInformation> existingEmail = Optional.ofNullable(accountDetailsRepositories.findByContactEmail(userRequest.getContactEmail()));
        Optional<AccountInformation> existingPhone = Optional.ofNullable(accountDetailsRepositories.findByContactPhoneNumber(userRequest.getContactPhone()));

        if (existingEmail.isPresent()) {
            throw new EmailAlreadyExistStep("Email Already Exist. ");

        } else if (existingPhone.isPresent()) {

            throw new PhoneNumberAlreadyExistStep("Phone Number Already Exist. ");

        } else {

            AccountDetailsGenarators accountDetailsGenarators = new AccountDetailsGenarators();

            String IFSC_CODE = accountDetailsGenarators.gereratedIFSC();
            String BANK_PIN = accountDetailsGenarators.generateBankCode();
            String PASSWORD = accountDetailsGenarators.generatePin();

            AccountInformation accountInformation = AccountInformation.builder()
                    .accountId(userIdGenerated)
                    .accountHolderName(userRequest.getAccountHolderName())
                    .contactEmail(userRequest.getContactEmail())
                    .contactPhone(userRequest.getContactPhone())
                    .gender(userRequest.getGender())
                    .isHaveUpiId(BANK_V3_NOTA_UPI_ID)
                    .contactAddress(userRequest.getContactAddress())
                    .stateOfOrigin(userRequest.getStateOfOrigin())
                    .pinCodeNumber(userRequest.getPinCodeNumber())
                    .currentLocation(userRequest.getCurrentLocation())
                    .designation(userRequest.getDesignation())
                    .country(userRequest.getCountry())
                    .password(PASSWORD)
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

            notificationsUtility.sendForTwilionLogin();
            notificationsUtility.ssendForCreateAccountNotification(accountInformation.getAccountHolderName());
            accountDetailsRepositories.save(accountInformation);

            UserResponse userRes = mapperToResponse.userInformationToUserResponse(accountInformation);
            userRes.setMessage(BANK_VI_ACCOUNT_CREATED);

            log.info("Account created successfully for user: {}", userRes.getAccountHolderName());
            return userRes;

        }
    }


    @Override
    public AccountUpdateDetailsResponse updateAccountDetails(AccountUpdatingDetailsRequest accountUpdatingDetailsRequest) {

        log.info("Updating account details for account number: {}", accountUpdatingDetailsRequest.getAccountNumber());

        Optional<AccountInformation> userInformation = Optional.ofNullable(accountDetailsRepositories.findByAccountNumber(
                accountUpdatingDetailsRequest.getAccountNumber()));

        if (userInformation.isPresent()) {
            log.info("User information found for account number: {}", accountUpdatingDetailsRequest.getAccountNumber());

            log.info("Updating account details for account holder: {}", accountUpdatingDetailsRequest.getAccountHolderName());
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


            log.info("Account details updated successfully for account holder: {}", accountUpdatingDetailsRequest.getAccountHolderName());

            notificationsUtility.sendForUpdateAccountDetails(accountUpdatingDetailsRequest.getAccountHolderName());
            accountDetailsRepositories.save(updateAccountDetails);
            MapperToUpdateResponse mapperToUpdateResponse = new MapperToUpdateResponse();

            return mapperToUpdateResponse.userInformationToUpdateAccountResponse(updateAccountDetails);

        } else

            log.info("Account not found for account number: {}", accountUpdatingDetailsRequest.getAccountNumber());
        throw new AccountNotFoundStep("The details you have entered are incorrect. There is no account with these details. Please double-check the information and try again.");
    }


    @Override
    public AccountDeletedSuccessResponse deleteAccount(AccountDeleteAccountDetailsRequest accountDetailsRequest) {
        log.info("Deleting account with account number: {}", accountDetailsRequest.getAccountNumber());

        AccountInformation accountInformation = accountDetailsRepositories.findByAccountIdAndIfscCode(
                accountDetailsRequest.getAccountNumber(),
                accountDetailsRequest.getContactEmail(),
                accountDetailsRequest.getPassword()
        );

        if (accountInformation != null) {
            log.info("Account found for deletion: {}", accountInformation);

            accountDetailsRepositories.delete(accountInformation);
            log.info("Account deleted for account number: {}", accountDetailsRequest.getAccountNumber());

            notificationsUtility.sendForDeletedAccount();
            log.info("Account deletion notification sent");

            return new AccountDeletedSuccessResponse("Account deleted successfully.");
        } else {
            log.info("Account not found for deletion. Account details: AccountNumber={}, ContactEmail={}, Password={}",
                    accountDetailsRequest.getAccountNumber(), accountDetailsRequest.getContactEmail(), accountDetailsRequest.getPassword());
            throw new AccountNotFoundStep("The details you have entered are incorrect. There is no account with these details. Please double-check the information and try again.");
        }
    }


    @Override
    public AccountDetailsResponse getYourAccountDetails(String accountNumber, String IFSCCode, String password) {

        log.info("Getting account details for accountNumber: {}", accountNumber);

        AccountInformation accountInformation = accountDetailsRepositories.findByAccountIdAndIfscCode(accountNumber
                , IFSCCode, password);
        AccountDetailsResponse accountDetailsResponse = new AccountDetailsResponse();

        if (accountInformation != null) {
            log.info("Account details found for accountNumber: {}", accountNumber);

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

            NetBankingInformation netBankingInformation = new NetBankingInformation();

            accountDetailsResponse.setPassword(netBankingInformation.getPassword());
            accountDetailsResponse.setNet_BANKING_ID(netBankingInformation.getNet_BANKING_ID());

            // need to fix
            UpiInformation upiInformation = new UpiInformation();

            accountDetailsResponse.setUPI_ID(upiInformation.getUpiId());
            accountDetailsResponse.setUPI_BALANCE(upiInformation.getUPI_BALANCE());

            return accountDetailsResponse;
        } else {

            log.info("Account not found for accountNumber: {}", accountNumber);
            throw new AccountNotFoundStep("The details you have entered are incorrect. There is no account with these details. Please double-check the information and try again.");
        }
    }

    @Override
    public CreditResponse creditYourMoney(CreditCredential creditCredential) {

        log.info("Crediting money for account number: {}", creditCredential.getAccountNumber());
        AccountInformation accountInformation = accountDetailsRepositories.findByAccountIdAndIfscCode(creditCredential.getAccountNumber(),
                creditCredential.getIfscCode(), creditCredential.getPassword());

        if (accountInformation != null) {
            log.info("Account found for crediting money. Account Number: {}", creditCredential.getAccountNumber());

            // transaction limit reached
            accountlLimitReached.validateDailyTransactionLimit(accountInformation);

            double creditedAmount = creditCredential.getCreditYourMoney();
            double currentBalance = accountInformation.getAccountBalance() + creditedAmount;

            accountInformation.setAccountBalance(currentBalance);
            accountInformation = accountDetailsRepositories.save(accountInformation);

            creditCredential.setCreditYourMoney(accountInformation.getAccountBalance());
            String autoGeneratedId = UUID.randomUUID().toString();

            notificationsUtility.sendForDebitedAccount(accountInformation.getAccountNumber(),
                    accountInformation.getAccountBalance(), accountInformation.getContactPhone());

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

            log.info("Money credited successfully for account number: {}. Current balance: {}", creditCredential.getAccountNumber(), currentBalance);
            return addingMoney;

        } else

            log.info("Account not found for crediting money. Account details: AccountNumber={}, IFSCCode={}, Password={}",
                    creditCredential.getAccountNumber(), creditCredential.getIfscCode(), creditCredential.getPassword());

        throw new AccountNotFoundStep("The details you have entered are incorrect. There is no account with these details. Please double-check the information and try again.");

    }

    @Override
    public DebitedResponse debitYourMoney(DebitCredential debitCredential) {

        log.info("Account found for debiting money. Account Number: {}", debitCredential.getAccountNumber());

        AccountInformation accountInformation = accountDetailsRepositories.findByAccountIdAndIfscCode(
                debitCredential.getAccountNumber(), debitCredential.getIfscCode(), debitCredential.getPassword());

        if (accountInformation != null) {
            double debitedAmount = debitCredential.getDebitYourMoney();

            accountlLimitReached.validateDailyTransactionLimit(accountInformation);

            if (debitedAmount >= 10000) {
                log.info("High amount of money transfer. Account Balance: {}, Debited Amount: {}", accountInformation.getAccountBalance(), debitedAmount);

                double currentBalance = accountInformation.getAccountBalance() - debitedAmount;

                if (currentBalance >= debitedAmount) {

                    notificationsUtility.sendForHighAmountOfMoneyTransfer();
                    accountInformation.setAccountBalance(currentBalance);
                    accountInformation = accountDetailsRepositories.save(accountInformation);
                    debitCredential.setDebitYourMoney(accountInformation.getAccountBalance());
                    String autoGeneratedId = UUID.randomUUID().toString();

                    notificationsUtility.sendForCredit(accountInformation.getAccountNumber(),
                            accountInformation.getAccountBalance(), accountInformation.getContactPhone());

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

                    log.info("Money debited successfully for account number: {}. Current balance: {}", debitCredential.getAccountNumber(), currentBalance);
                    return miniMoney;

                } else {

                    log.info("Insufficient balance to complete the transaction. Account Balance: {}, Debited Amount: {}", accountInformation.getAccountBalance(), debitedAmount);
                    throw new InSufficientBalance("Insufficient balance to complete the transaction.");
                }
            } else {

                debitedAmount = debitCredential.getDebitYourMoney();
                double currentBalance = accountInformation.getAccountBalance() - debitedAmount;

                if (currentBalance >= debitedAmount) {
                    accountInformation.setAccountBalance(currentBalance);
                    accountInformation = accountDetailsRepositories.save(accountInformation);
                    debitCredential.setDebitYourMoney(accountInformation.getAccountBalance());
                    String autoGeneratedId = UUID.randomUUID().toString();

                    notificationsUtility.sendForCredit(accountInformation.getAccountNumber(),
                            accountInformation.getAccountBalance(), accountInformation.getContactPhone());

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

                    log.info("Money debited successfully for account number: {}. Current balance: {}", debitCredential.getAccountNumber(), currentBalance);
                    return miniMoney;
                }
            }
        }

        log.info("Account not found for debiting money. Account details: AccountNumber={}, IFSCCode={}, Password={}",
                debitCredential.getAccountNumber(), debitCredential.getIfscCode(), debitCredential.getPassword());

        throw new AccountNotFoundStep("The details you have entered are incorrect. There is no account with these details. Please double-check the information and try again.");
    }


    @Override
    public BalanceEnquiryResponse balanceEnquiry(BalanceEnquireyRequest balanceEnquireyRequest) {
        log.info("Received request for balance enquiry: {}", balanceEnquireyRequest);

        AccountInformation accountInformation = accountDetailsRepositories.findByAccountIdAndIfscCode(
                balanceEnquireyRequest.getAccountNumber(),
                balanceEnquireyRequest.getIfscCode(),
                balanceEnquireyRequest.getPassword()
        );

        log.info("Account information found for the request.");

        NotificationsUtility notificationsUtility = new NotificationsUtility();
        notificationsUtility.sendForBalanceEnquiry(accountInformation.getAccountNumber(),
                accountInformation.getAccountBalance(), accountInformation.getContactPhone());

        log.info("Sent balance enquiry notification.");

        String balanceId = UUID.randomUUID().toString();
        BalanceEnquiryResponse response = new BalanceEnquiryResponse();
        response.setBalanceId(balanceId);
        response.setAccountNumber(accountInformation.getAccountNumber());
        response.setStatusMessage(BANK_VI_ACCOUNT_BALANCE_STATUS);
        response.setYourBalance(accountInformation.getAccountBalance());
        response.setLocalDateTime(LocalDateTime.now());

        log.info("Balance enquiry successful.");

        return response;
    }


    // adding money from account balance to UPI, account -> UPI
    @Override
    public AddMoneyFromAccountToUPIResponse payUsingUpi(AddMoneyFromAccountToUPIRequest addMoneyFromAccountToUPIRequest) {
        log.info("Received request to pay using UPI: {}", addMoneyFromAccountToUPIRequest);

        AccountInformation accountInformation = accountDetailsRepositories.findByAccountNumber(addMoneyFromAccountToUPIRequest.getAccountNumber());
        UpiInformation upiInformation = upiDetailsRepositories.findByUpiId(addMoneyFromAccountToUPIRequest.getUpiId());

        if (upiInformation != null && accountInformation != null) {
            log.info("UPI and account information found for the request.");

            accountlLimitReached.validateDailyTransactionLimit(accountInformation);
            log.info("Validated daily transaction limit for the account.");

            if (accountInformation.getAccountBalance() > addMoneyFromAccountToUPIRequest.getPayMoney()) {
                double getFromUPI = addMoneyFromAccountToUPIRequest.getPayMoney();
                double fromMainAccount = accountInformation.getAccountBalance();
                double leftMoneyForMainAccount = fromMainAccount - getFromUPI;

                accountInformation.setAccountBalance(leftMoneyForMainAccount);
                accountDetailsRepositories.save(accountInformation);

                upiInformation.setUPI_BALANCE(getFromUPI);
                upiDetailsRepositories.save(upiInformation);

                AddMoneyFromAccountToUPIResponse payUsingUpiResponse = new AddMoneyFromAccountToUPIResponse();
                payUsingUpiResponse.setResponseMessage(SUCCESS_PAY_MONEY_FROM_UPI);
                payUsingUpiResponse.setStatus(SUCCESS_STATUS);

                log.info("Payment using UPI successful.");

                return payUsingUpiResponse;
            } else {
                log.error("Insufficient balance for the payment.");
                throw new InSufficientBalance("Insufficient Balance..");
            }
        } else {
            log.error("UPI or account information not found for the request.");
            throw new DetailsNotFountException("The details you have entered are incorrect. There is no account with these details. Please double-check the information and try again.");
        }
    }


    public AddMoneyToUPIFromAccountResponse addingMoneyFromAccountNumberToUpi(AddMoneyToUPIFromAccountRequest addMoneyToUPIFromAccountRequest) {
        log.info("Adding money from account number {} to UPI {}.", addMoneyToUPIFromAccountRequest.getAccountNumber(), addMoneyToUPIFromAccountRequest.getUpiId());

        AccountInformation accountInformation = accountDetailsRepositories
                .findByAccountNumberAndPassword(
                        addMoneyToUPIFromAccountRequest.getAccountNumber(),
                        addMoneyToUPIFromAccountRequest.getPassword());

        UpiInformation upiInformation = upiDetailsRepositories.findByUpiId(addMoneyToUPIFromAccountRequest.getUpiId());

        try {


            if (accountInformation != null && upiInformation != null) {
                log.info("Account and UPI found for the transaction.");

                accountlLimitReached.validateDailyTransactionLimit(accountInformation);

                double moneyGetFromMainAccount = accountInformation.getAccountBalance();

                double gettingMoneyFromUpi = addMoneyToUPIFromAccountRequest.getAddedFromUpi();
                double adding = moneyGetFromMainAccount + gettingMoneyFromUpi;

                accountInformation.setAccountBalance(adding);

                accountDetailsRepositories.save(accountInformation);

                AddMoneyToUPIFromAccountResponse addMoneyToUPIFromAccountResponse = new AddMoneyToUPIFromAccountResponse();
                addMoneyToUPIFromAccountResponse.setStatus(SUCCESS_ADD_MONEY_TO_UPI_FROM_MAIN_ACCOUNT);

                log.info("Money added successfully from account number {} to UPI {}.", addMoneyToUPIFromAccountRequest.getAccountNumber(), addMoneyToUPIFromAccountRequest.getUpiId());

                return addMoneyToUPIFromAccountResponse;
            }

        } catch (NullPointerException e) {

            log.warn("NullPointerException occurred: {}", e.getMessage());
            log.warn("There is no records...");

            System.out.println("There is no records...");
        }

        log.info("Account or UPI not found for the transaction. AccountNumber={}, UPIId={}", addMoneyToUPIFromAccountRequest.getAccountNumber(), addMoneyToUPIFromAccountRequest.getUpiId());

        throw new AccountNotFoundStep("The details you have entered are incorrect. There is no account with these details. Please double-check the information and try again.");
    }

    @Override
    public UpdateAmountResponse updateAmountInPerson(UpdateAmountManually updateAmountManually) {

        log.info("Updating amount in person for account number: {}", updateAmountManually.getAccountNumber());

        AccountInformation accountInformation = accountDetailsRepositories
                .findByAccountNumber(updateAmountManually.getAccountNumber());

        if (accountInformation != null) {

            log.info("Account found for updating amount in person. Account Number: {}", updateAmountManually.getAccountNumber());
            accountInformation.setAccountBalance(updateAmountManually.getAccountBalance());
            accountDetailsRepositories.save(accountInformation);

            log.info("Account balance updated in person. New balance: {}", updateAmountManually.getAccountBalance());
        } else {

            log.info("Account not found for updating amount in person. Account Number: {}", updateAmountManually.getAccountNumber());

            throw new AccountNotFoundStep("The details you have entered are incorrect. " +
                    "There is no account with these details. Please double-check the information and try again.");
        }

        UpdateAmountResponse updateAmountResponse = new UpdateAmountResponse();
        updateAmountResponse.setMessage("In person, an ATM has credited your account, adding a physical touch to your financial update.");
        return updateAmountResponse;
    }
}



