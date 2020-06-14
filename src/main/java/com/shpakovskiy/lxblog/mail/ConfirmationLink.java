package com.shpakovskiy.lxblog.mail;

import com.shpakovskiy.lxblog.entity.User;
import com.shpakovskiy.lxblog.security.registration.RegistrationQueueService;
import org.springframework.stereotype.Component;

@Component
public class ConfirmationLink {

    private static final String HOST_NAME = "localhost:8080";       //TODO: Replace with properties file data
    private static final String MIDDLE_ADDRESS = "/auth/confirm/";

    private final RegistrationQueueService registrationQueueService;
    private final MailSender mailSender;

    public ConfirmationLink(RegistrationQueueService registrationQueueService,
                            MailSender mailSender) {

        this.registrationQueueService = registrationQueueService;
        this.mailSender = mailSender;
    }

    public void sendConfirmationLink(User user) {
        String token = registrationQueueService.getUsersToken(user);
        mailSender.sendMessage(new Message.Builder()
                .receiverAddress(user.getEmail())
                .subject("Registration confirmation")
                .text("Confirm your LXBlog registration.\n" +
                        "Just click this link: " + buildLink(token) +
                        "\nEnjoy!")
                .build());
    }

    private String buildLink(String token) {
        return HOST_NAME + MIDDLE_ADDRESS + token;
    }
}
