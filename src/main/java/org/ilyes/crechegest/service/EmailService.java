package org.ilyes.crechegest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final SimpleMailMessage templateMessage;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
        this.templateMessage = new SimpleMailMessage();
        this.templateMessage.setFrom("no-reply@crechegest.com");
    }

    public void sendCredentialsEmail(String toEmail, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your CrecheGest Account Credentials");
        message.setText(String.format(
                "Welcome to CrecheGest!\n\n" +
                        "Your account has been created.\n" +
                        "Password: %s\n\n" +
                        "Please change your password after first login.",
                password
        ));
        mailSender.send(message);
    }
}