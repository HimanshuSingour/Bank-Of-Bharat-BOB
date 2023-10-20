package com.syc.finance.v1.bharat.service;
import com.syc.finance.v1.bharat.Mapper.MapperToResponse;
import com.syc.finance.v1.bharat.Mapper.MapperToUpdateResponse;
import com.syc.finance.v1.bharat.Notifications.*;
import com.syc.finance.v1.bharat.Utils.AccountDeletedSuccessResponse;
import com.syc.finance.v1.bharat.Utils.AccountDetailsGenarators;
import com.syc.finance.v1.bharat.Utils.AccountlLimitReached;
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
import static com.syc.finance.v1.bharat.Utils.AccountDetailsConstants.*;
import com.syc.finance.v1.bharat.entity.UpiInformation;
import com.syc.finance.v1.bharat.exceptions.*;
import com.syc.finance.v1.bharat.repository.TransactionHistoryRepository;
import com.syc.finance.v1.bharat.repository.AccountDetailsRepositories;
import com.syc.finance.v1.bharat.repository.UPIDetailsRepositories;
import com.syc.finance.v1.bharat.service.AccountService;
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
            return userRes;

        }
    }


    @Override
    public AccountUpdateDetailsResponse updateAccountDetails(AccountUpdatingDetailsRequest accountUpdatingDetailsRequest) {

        Optional<AccountInformation> userInformation = Optional.ofNullable(accountDetailsRepositories.findByAccountNumber(
                accountUpdatingDetailsRequest.getAccountNumber()));

        if (userInformation.isPresent()) {

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

            notificationsUtility.sendForUpdateAccountDetails(accountUpdatingDetailsRequest.getAccountHolderName());

            accountDetailsRepositories.save(updateAccountDetails);
            MapperToUpdateResponse mapperToUpdateResponse = new MapperToUpdateResponse();

            return mapperToUpdateResponse.userInformationToUpdateAccountResponse(updateAccountDetails);

        } else

            throw new AccountNotFoundStep("The details you have entered are incorrect. There is no account with these details. Please double-check the information and try again.");
    }


    @Override
    public AccountDeletedSuccessResponse deleteAccount(AccountDeleteAccountDetailsRequest accountDetailsRequest) {

        AccountInformation accountInformation = accountDetailsRepositories.findByAccountIdAndIfscCode(
                accountDetailsRequest.getAccountNumber(),
                accountDetailsRequest.getContactEmail(),
                accountDetailsRequest.getPassword()

        );

        if (accountInformation != null) {

            accountDetailsRepositories.delete(accountInformation);
            notificationsUtility.sendForDeletedAccount();

            return new AccountDeletedSuccessResponse("Account deleted successfully.");
        } else {
            throw new AccountNotFoundStep("The details you have entered are incorrect. There is no account with these details. Please double-check the information and try again.");
        }
    }


    @Override
    public AccountDetailsResponse getYourAccountDetails(String accountNumber, String IFSCCode, String password) {

        AccountInformation accountInformation = accountDetailsRepositories.findByAccountIdAndIfscCode(accountNumber
                , IFSCCode, password);
        AccountDetailsResponse accountDetailsResponse = new AccountDetailsResponse();

        if (accountInformation != null) {

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
            throw new AccountNotFoundStep("The details you have entered are incorrect. There is no account with these details. Please double-check the information and try again.");
        }
    }

    @Override
    public CreditResponse creditYourMoney(CreditCredential creditCredential) {

        AccountInformation accountInformation = accountDetailsRepositories.findByAccountIdAndIfscCode(creditCredential.getAccountNumber(),
                creditCredential.getIfscCode(), creditCredential.getPassword());

        if (accountInformation != null) {

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
            return addingMoney;
        } else
            throw new AccountNotFoundStep("The details you have entered are incorrect. There is no account with these details. Please double-check the information and try again.");

    }

    @Override
    public DebitedResponse debitYourMoney(DebitCredential debitCredential) {

        AccountInformation accountInformation = accountDetailsRepositories.findByAccountIdAndIfscCode(
                debitCredential.getAccountNumber(), debitCredential.getIfscCode(), debitCredential.getPassword());

        if (accountInformation != null) {
            double debitedAmount = debitCredential.getDebitYourMoney();

            accountlLimitReached.validateDailyTransactionLimit(accountInformation);

            if (debitedAmount >= 10000) {



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
                    return miniMoney;

                }
                else {

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

                    return miniMoney;
                }
            }
        }

        throw new AccountNotFoundStep("The details you have entered are incorrect. There is no account with these details. Please double-check the information and try again.");
    }


    @Override
    public BalanceEnquiryResponse balanceEnquiry(BalanceEnquireyRequest balanceEnquireyRequest) {

        AccountInformation accountInformation = accountDetailsRepositories.findByAccountIdAndIfscCode(balanceEnquireyRequest.getAccountNumber(),
                balanceEnquireyRequest.getIfscCode(), balanceEnquireyRequest.getPassword());

        NotificationsUtility notificationsUtility = new NotificationsUtility();
        notificationsUtility.sendForBalanceEnquiry(accountInformation.getAccountNumber(),
                accountInformation.getAccountBalance(), accountInformation.getContactPhone());

        String balanceId = UUID.randomUUID().toString();
        BalanceEnquiryResponse response = new BalanceEnquiryResponse();
        response.setBalanceId(balanceId);
        response.setAccountNumber(accountInformation.getAccountNumber());
        response.setStatusMessage(BANK_VI_ACCOUNT_BALANCE_STATUS);
        response.setYourBalance(accountInformation.getAccountBalance());
        response.setLocalDateTime(LocalDateTime.now());
        return response;
    }


    // adding money from account balance to UPI, account -> UPI
    @Override
    public AddMoneyFromAccountToUPIResponse payUsingUpi(AddMoneyFromAccountToUPIRequest addMoneyFromAccountToUPIRequest) {

        AccountInformation accountInformation = accountDetailsRepositories
                .findByAccountNumber(addMoneyFromAccountToUPIRequest.getAccountNumber());

        UpiInformation upiInformation = upiDetailsRepositories.findByUpiId(addMoneyFromAccountToUPIRequest.getUpiId());

        if (upiInformation != null && accountInformation != null) {

            accountlLimitReached.validateDailyTransactionLimit(accountInformation);
            if (accountInformation.getAccountBalance() > addMoneyFromAccountToUPIRequest.getPayMoney()) {

                double getFormUPI = addMoneyFromAccountToUPIRequest.getPayMoney();
                double fromMainAccount = accountInformation.getAccountBalance();
                double leftMoneyForMainAccount = fromMainAccount - getFormUPI;

                accountInformation.setAccountBalance(leftMoneyForMainAccount);
                accountDetailsRepositories.save(accountInformation);

                upiInformation.setUPI_BALANCE(getFormUPI);
                upiDetailsRepositories.save(upiInformation);

                AddMoneyFromAccountToUPIResponse payUsingUpiResponse = new AddMoneyFromAccountToUPIResponse();
                payUsingUpiResponse.setResponseMessage(SUCCESS_PAY_MONEY_FROM_UPI);
                payUsingUpiResponse.setStatus(SUCCESS_STATUS);
                return payUsingUpiResponse;
            } else {

                throw new InSufficientBalance("Insufficient Balance..");
            }
        }
        throw new DetailsNotFountException("The details you have entered are incorrect. There is no account with these details. Please double-check the information and try again.");
    }


    public AddMoneyToUPIFromAccountResponse addingMoneyFromAccountNumberToUpi(AddMoneyToUPIFromAccountRequest addMoneyToUPIFromAccountRequest) {

        AccountInformation accountInformation = accountDetailsRepositories
                .findByAccountNumberAndPassword(
                        addMoneyToUPIFromAccountRequest.getAccountNumber(),
                        addMoneyToUPIFromAccountRequest.getPassword());

        UpiInformation upiInformation = upiDetailsRepositories.findByUpiId(addMoneyToUPIFromAccountRequest.getUpiId());

        try {


            if (accountInformation != null && upiInformation != null) {

                accountlLimitReached.validateDailyTransactionLimit(accountInformation);

                double moneyGetFromMainAccount = accountInformation.getAccountBalance();

                double gettingMoneyFromUpi = addMoneyToUPIFromAccountRequest.getAddedFromUpi();
                double adding = moneyGetFromMainAccount + gettingMoneyFromUpi;

                accountInformation.setAccountBalance(adding);

                accountDetailsRepositories.save(accountInformation);

                AddMoneyToUPIFromAccountResponse addMoneyToUPIFromAccountResponse = new AddMoneyToUPIFromAccountResponse();
                addMoneyToUPIFromAccountResponse.setStatus(SUCCESS_ADD_MONEY_TO_UPI_FROM_MAIN_ACCOUNT);
                return addMoneyToUPIFromAccountResponse;
            }

        } catch (NullPointerException e) {

            System.out.println("There is no records...");
        }

        throw new AccountNotFoundStep("The details you have entered are incorrect. There is no account with these details. Please double-check the information and try again.");
    }
}



