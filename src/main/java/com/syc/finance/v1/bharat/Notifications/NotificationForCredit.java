package com.syc.finance.v1.bharat.Notifications;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

import static com.syc.finance.v1.bharat.Utils.AccountDetailsConstants.TWILIO_PHONE_NUMBER;

@Slf4j
public class NotificationForCredit {

    public void sendForDebitedAccount(String accountNumber, double accountBalance, String phoneNumber) {
        String messageBody = "Dear Customer, Your money has been Credited from your account " + accountNumber + ", and this transaction" +
                " has been successfully completed. Current Balance : " + accountBalance;


        PhoneNumber recipientNumber = new com.twilio.type.PhoneNumber("+91"+phoneNumber);
        PhoneNumber twilioNumber = new com.twilio.type.PhoneNumber(TWILIO_PHONE_NUMBER);

        try {
            // Create the SMS message
            Message message = Message.creator(recipientNumber, twilioNumber, messageBody).create();

            if (message.getStatus() == Message.Status.SENT) {
                log.info("SMS sent successfully!");
            } else {
                log.error("Failed to send SMS: " + message.getErrorMessage());
            }
        } catch (Exception e) {
            log.error("An error occurred while sending SMS: " + e.getMessage(), e);
        }
    }
}
