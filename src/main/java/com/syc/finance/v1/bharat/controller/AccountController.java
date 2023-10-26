package com.syc.finance.v1.bharat.controller;


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
import com.syc.finance.v1.bharat.service.AccountService;
import com.syc.finance.v1.bharat.service.TransactionService.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;

@RestController
@RequestMapping("finance/v1/bank/v4/bharat")

@CrossOrigin(origins = "http://localhost:4200/")
public class AccountController {

    @Autowired
    private AccountService userService;

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/create-account")
    ResponseEntity<UserResponse> createYourAccount(@RequestBody UserRequest userRequest){
        UserResponse userResponse = userService.createAccount(userRequest);
        return new ResponseEntity<UserResponse>(userResponse , HttpStatus.CREATED);
    }

    @PutMapping("/update-account-details")
    ResponseEntity<AccountUpdateDetailsResponse> updateAnAccountDetails(@RequestBody AccountUpdatingDetailsRequest accountDetailsRequest){
        AccountUpdateDetailsResponse accountUpdateDetailsResponse = null;
        try {
            accountUpdateDetailsResponse = userService.updateAccountDetails(accountDetailsRequest);
        } catch (AccountNotFoundException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<AccountUpdateDetailsResponse>(accountUpdateDetailsResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete-account")
    ResponseEntity<AccountDeletedSuccessResponse> deleteAccount(@RequestBody AccountDeleteAccountDetailsRequest accountDetailsRequest){
        AccountDeletedSuccessResponse accountDeletedSuccessResponse = userService.deleteAccount(accountDetailsRequest);
        return new ResponseEntity<AccountDeletedSuccessResponse>(accountDeletedSuccessResponse , HttpStatus.ACCEPTED);
    }
    @GetMapping("/get-account-details/{accountNumber}/{IFSCCode}/{password}")
    ResponseEntity<AccountDetailsResponse> getAccountDetails(@PathVariable String accountNumber,
                                                             @PathVariable String IFSCCode,
                                                             @PathVariable String password){

        AccountDetailsResponse accountDetailsResponse = userService.getYourAccountDetails(accountNumber , IFSCCode , password);
        return new ResponseEntity<AccountDetailsResponse>(accountDetailsResponse , HttpStatus.ACCEPTED);
    }

    @GetMapping("/balance-enquiry")
    ResponseEntity<BalanceEnquiryResponse> balanceMoney(@RequestBody BalanceEnquireyRequest balanceEnquireyRequest){
        BalanceEnquiryResponse response = userService.balanceEnquiry(balanceEnquireyRequest);
        return new ResponseEntity<BalanceEnquiryResponse>(response , HttpStatus.ACCEPTED);
    }

    @GetMapping("/credit-money")
    ResponseEntity<CreditResponse> creditMoney(@RequestBody CreditCredential creditCredential){
        CreditResponse response = userService.creditYourMoney(creditCredential);
        return new ResponseEntity<CreditResponse>(response , HttpStatus.ACCEPTED);
    }

    @GetMapping("/debit-money")
    ResponseEntity<DebitedResponse> debitMoney(@RequestBody DebitCredential balanceEnquireyRequest){
        DebitedResponse response = userService.debitYourMoney(balanceEnquireyRequest);
        return new ResponseEntity<DebitedResponse>(response , HttpStatus.ACCEPTED);
    }

    // upi

    @PostMapping("/pay-money-from-upi")
    ResponseEntity<AddMoneyFromAccountToUPIResponse> payFromUpiId(@RequestBody AddMoneyFromAccountToUPIRequest addMoneyFromAccountToUPIRequest){
        AddMoneyFromAccountToUPIResponse response = userService.payUsingUpi(addMoneyFromAccountToUPIRequest);
        return new ResponseEntity<AddMoneyFromAccountToUPIResponse>(response , HttpStatus.ACCEPTED);
    }

    @PostMapping("/add-money-to-upi-from-bank")
    ResponseEntity<AddMoneyToUPIFromAccountResponse> payTo(@RequestBody AddMoneyToUPIFromAccountRequest addMoneyToUPIFromAccountRequest){
        AddMoneyToUPIFromAccountResponse response = userService.addingMoneyFromAccountNumberToUpi(addMoneyToUPIFromAccountRequest);
        return new ResponseEntity<AddMoneyToUPIFromAccountResponse>(response , HttpStatus.ACCEPTED);
    }

    @PutMapping("/update/money")
    ResponseEntity<UpdateAmountResponse> addMoneyInPerson(@RequestBody UpdateAmountManually updateAmountManually){
        UpdateAmountResponse response = userService.updateAmountInPerson(updateAmountManually);
        return new ResponseEntity<UpdateAmountResponse>(response , HttpStatus.ACCEPTED);
    }
}
