package com.shpakovskiy.lxblog.controllers;

import com.shpakovskiy.lxblog.security.registration.TokenValidator;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class ConfirmationController {

    private final TokenValidator tokenValidator;

    public ConfirmationController(TokenValidator tokenValidator) {
        this.tokenValidator = tokenValidator;
    }

    @GetMapping("/auth/confirm/{token}")
    public HttpStatus confirmUser(@PathVariable String token, HttpServletResponse response) {
        tokenValidator.validateToken(token, response);
        return HttpStatus.OK;
    }

    @RequestMapping("/no-such-token")
    public HttpStatus noSuchUser() {
        return HttpStatus.NOT_FOUND;
    }

    @RequestMapping("/token-expired")
    public HttpStatus tokenExpired() {
        return HttpStatus.NOT_FOUND;
    }
}
