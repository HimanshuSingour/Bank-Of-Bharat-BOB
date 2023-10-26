package com.syc.finance.v1.bharat.service;

import com.syc.finance.v1.bharat.dto.Update.UpdateAmountManually;
import com.syc.finance.v1.bharat.dto.Update.UpdateAmountResponse;
import com.syc.finance.v1.bharat.utils.AccountDeletedSuccessResponse;
import com.syc.finance.v1.bharat.dto.*;
import com.syc.finance.v1.bharat.dto.Accounts.*;
import com.syc.finance.v1.bharat.dto.BalanceEnquiry.BalanceEnquireyRequest;
import com.syc.finance.v1.bharat.dto.BalanceEnquiry.BalanceEnquiryResponse;
import com.syc.finance.v1.bharat.dto.Credit.CreditCredential;
import com.syc.finance.v1.bharat.dto.Credit.CreditResponse;
import com.syc.finance.v1.bharat.dto.Debit.DebitCredential;
import com.syc.finance.v1.bharat.dto.Debit.DebitedResponse;
import com.syc.finance.v1.bharat.dto.UPIPay.AddMoneyToUPIFromAccountRequest;
import com.syc.finance.v1.bharat.dto.UPIPay.AddMoneyToUPIFromAccountResponse;
import com.syc.finance.v1.bharat.dto.UPIPay.AddMoneyFromAccountToUPIRequest;
import com.syc.finance.v1.bharat.dto.UPIPay.AddMoneyFromAccountToUPIResponse;

import javax.security.auth.login.AccountNotFoundException;

public interface AccountService {

     UserResponse createAccount(UserRequest userRequest);
     AccountUpdateDetailsResponse updateAccountDetails(AccountUpdatingDetailsRequest accountUpdatingDetailsRequest) throws AccountNotFoundException;
     AccountDeletedSuccessResponse deleteAccount(AccountDeleteAccountDetailsRequest accountDeleteAccountDetailsRequest);
     AccountDetailsResponse getYourAccountDetails(String accountNumber , String IFSCCode , String password);
     CreditResponse creditYourMoney(CreditCredential creditCredential);
     DebitedResponse debitYourMoney(DebitCredential debitCredential);
     BalanceEnquiryResponse balanceEnquiry(BalanceEnquireyRequest balanceEnquireyRequest);
     AddMoneyFromAccountToUPIResponse payUsingUpi(AddMoneyFromAccountToUPIRequest addMoneyFromAccountToUPIRequest);
     AddMoneyToUPIFromAccountResponse addingMoneyFromAccountNumberToUpi(AddMoneyToUPIFromAccountRequest addMoneyToUPIFromAccountRequest);
     UpdateAmountResponse updateAmountInPerson(UpdateAmountManually updateAmountManually);

}
