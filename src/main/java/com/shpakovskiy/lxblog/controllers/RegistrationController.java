package com.shpakovskiy.lxblog.controllers;

import com.shpakovskiy.lxblog.entity.User;
import com.shpakovskiy.lxblog.mail.ConfirmationLink;
import com.shpakovskiy.lxblog.security.registration.ExistenceChecker;
import com.shpakovskiy.lxblog.security.registration.RegistrationQueueService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class RegistrationController {

    private final ExistenceChecker existenceChecker;
    private final RegistrationQueueService registrationQueueService;
    private final ConfirmationLink confirmationLink;

    public RegistrationController(ExistenceChecker existenceChecker,
                                  RegistrationQueueService registrationQueueService,
                                  ConfirmationLink confirmationLink) {

        this.existenceChecker = existenceChecker;
        this.registrationQueueService = registrationQueueService;
        this.confirmationLink = confirmationLink;
    }

    @PostMapping("/new")
    public HttpStatus newUser(@RequestBody User user, HttpServletResponse response) {
        existenceChecker.checkExistence(user, response);
        registrationQueueService.addItem(user);
        confirmationLink.sendConfirmationLink(user);
        return HttpStatus.OK;
    }

    @RequestMapping("/user-already-exists")
    public HttpStatus userExists() {
        return HttpStatus.CONFLICT;
    }

    @RequestMapping("/user-not-exist")
    public HttpStatus userNotExist() {
        return HttpStatus.UNAUTHORIZED;
    }

    @RequestMapping("/no-auth-data")
    public HttpStatus noAuthData() {
        return HttpStatus.EXPECTATION_FAILED;
    }
}