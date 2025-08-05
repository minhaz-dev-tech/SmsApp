package com.example.smsfoword;

import android.util.Log;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

public class GMailSender {

    private static final String TAG = "GMailSender";

    private final Mailer mailer;

    public GMailSender(String user, String password) {
        mailer = MailerBuilder
                .withSMTPServer("smtp.gmail.com", 587, user, password)
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .buildMailer();
    }

    /**
     * Synchronously send email. Do NOT call this on the main thread!
     */
    public void sendMail(String subject, String body, String sender, String recipients) {
        Email email = EmailBuilder.startingBlank()
                .from(sender)
                .to(recipients)
                .withSubject(subject)
                .withPlainText(body)
                .buildEmail();

        mailer.sendMail(email);
        Log.d(TAG, "Email sent successfully");
    }

    /**
     * Asynchronously send email on a background thread.
     */
    public void sendMailAsync(final String subject, final String body, final String sender, final String recipients) {
        new Thread(() -> {
            try {
                sendMail(subject, body, sender, recipients);
            } catch (Exception e) {
                Log.e(TAG, "Failed to send email", e);
            }
        }).start();
    }
}