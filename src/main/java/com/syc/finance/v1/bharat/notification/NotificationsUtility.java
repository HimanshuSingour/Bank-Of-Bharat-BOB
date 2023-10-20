package com.syc.finance.v1.bharat.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Slf4j
@Component
public class NotificationsUtility {

    NotificationConfig notificationConfig = new NotificationConfig();

    public void sendForUPIIdCreation(String upiId) {
        String messageBody = "Your UPI has been created successfully: " + upiId;
        notificationConfig.sendSMS(messageBody);
    }

    public void sendForNetBankingCreationAlert() {
        String messageBody = "You have requested the creation of a UPI ID. Note: To create a UPI ID, you need to first create a NetBanking ID, and then you can proceed.";
        notificationConfig.sendSMS(messageBody);
    }

    public void sendForUpdateAccountDetails(String accountNumber) {
        String messageBody = "Your account details have been successfully updated at Bank of Bharat. Account Holder's Name: " + accountNumber;
        notificationConfig.sendSMS(messageBody);
    }

    public void sendForTwilionLogin() {
        String se = "https://www.twilio.com/try-twilio";
        String messageBody = "This Application is in the production phase. For now, if you want all updates and notifications about money " +
                "transactions and account creation, please log in to this link : " + se + "using your mobile number." +
                " Note that your mobile number should be the same as the one associated with your bank account." ;
        notificationConfig.sendSMS(messageBody);
    }

    public void sendForDeletedAccount() {
        String messageBody = "Your Account has been deleted successfully !!";
        notificationConfig.sendSMS(messageBody);
    }


    public void sendForNetBankingCreation() {
        String messageBody = "Your NetBanking-ID has been created successfully, Proceeding for UPI-ID creation, Congratulation";
        notificationConfig.sendSMS(messageBody);
    }

    public void sendForCredit(String accountNumber, double accountBalance, String phoneNumber) {
        String messageBody = "Dear Customer, Your money has been Debited from your account " + accountNumber + ", and this transaction" +
                " has been successfully completed. Current Balance : " + accountBalance;
        notificationConfig.sendSMS(messageBody);
    }

    public void sendForDebitedAccount(String accountNumber, double accountBalance, String phoneNumber) {
        String messageBody = "Dear Customer, Your money has been Credited from your account " + accountNumber + ", and this transaction" +
                " has been successfully completed. Current Balance : " + accountBalance;
        notificationConfig.sendSMS(messageBody);
    }

    public void ssendForCreateAccountNotification(String accountHolderName) {
        String messageBody = "Your account has been successfully created at Bank of Bharat, " +
                "and your bank account number is:  " + accountHolderName;
        notificationConfig.sendSMS(messageBody);
    }

    public void sendForBalanceEnquiry(String accountNumber, double accountBalance, String phoneNumber){
        String messageBody = "Dear Customer, Your account balance for account number " +
                accountNumber + " is " + accountBalance + "\n" + LocalDateTime.now();
        notificationConfig.sendSMS(messageBody);
    }

    public void sendForHighAmountOfMoneyTransfer(){
        String messageBody = "Dear customer, you have recently made a high-value transaction. " +
                "If you did not authorize this transaction, please contact us at 626-676-9479. ";
        notificationConfig.sendSMS(messageBody);
    }

}
