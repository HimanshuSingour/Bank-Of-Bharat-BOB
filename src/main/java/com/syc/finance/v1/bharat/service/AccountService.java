package com.syc.finance.v1.bharat.service;

import com.syc.finance.v1.bharat.Utils.AccountDeletedSuccessResponse;
import com.syc.finance.v1.bharat.dto.*;
import com.syc.finance.v1.bharat.dto.Accounts.*;
import com.syc.finance.v1.bharat.dto.BalanceEnquiry.BalanceEnquireyRequest;
import com.syc.finance.v1.bharat.dto.BalanceEnquiry.BalanceEnquiryResponse;
import com.syc.finance.v1.bharat.dto.Credit.CreditCredential;
import com.syc.finance.v1.bharat.dto.Credit.CreditResponse;
import com.syc.finance.v1.bharat.dto.Debit.DebitCredential;
import com.syc.finance.v1.bharat.dto.Debit.DebitedResponse;

import javax.security.auth.login.AccountNotFoundException;

public interface AccountService {

     UserResponse createAccount(UserRequest userRequest);
     AccountUpdateDetailsResponse updateAccountDetails(AccountUpdatingDetailsRequest accountUpdatingDetailsRequest) throws AccountNotFoundException;
     AccountDeletedSuccessResponse deleteAccount(AccountDeleteAccountDetailsRequest accountDeleteAccountDetailsRequest);
     AccountDetailsResponse getYourAccountDetails(String accountNumber , String IFSCCode , String password);
     CreditResponse creditYourMoney(CreditCredential creditCredential);
     DebitedResponse debitYourMoney(DebitCredential debitCredential);
     BalanceEnquiryResponse balanceEnquiry(BalanceEnquireyRequest balanceEnquireyRequest);

}
