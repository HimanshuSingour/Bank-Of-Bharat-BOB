package com.syc.finance.v1.bharat.notification;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.syc.finance.v1.bharat.constants.AccountDetailsConstants.TWILIO_PHONE_NUMBER;


@Component
@Slf4j
public class NotificationConfig {

    public void sendSMS(String messageBody) {
        PhoneNumber recipientNumber = new com.twilio.type.PhoneNumber("+916266769479");
        PhoneNumber twilioNumber = new com.twilio.type.PhoneNumber(TWILIO_PHONE_NUMBER);

        try {
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
