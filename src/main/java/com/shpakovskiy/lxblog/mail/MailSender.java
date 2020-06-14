package com.shpakovskiy.lxblog.mail;


import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
public class MailSender {

    private static final String SERVER_ADDRESS = "noreply.lxblog@gmail.com";
    private static final String SERVER_PASSWORD = "1234567eight";               //FIXME: For debug purpose only!

    public void sendMessage(Message message) {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", "smtp.gmail.com");                     //TODO: Replace with properties file data
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties,  new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SERVER_ADDRESS, SERVER_PASSWORD);
            }
        });

        session.setDebug(true);

        try {
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(SERVER_ADDRESS));
            mimeMessage.addRecipient(javax.mail.Message.RecipientType.TO,
                    new InternetAddress(message.getReceiverAddress()));
            mimeMessage.setSubject(message.getSubject());
            mimeMessage.setText(message.getText());
            Transport.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}