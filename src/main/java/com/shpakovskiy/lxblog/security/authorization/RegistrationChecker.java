package com.shpakovskiy.lxblog.security.authorization;

import com.shpakovskiy.lxblog.entity.User;
import com.shpakovskiy.lxblog.repository.UserRepository;
import com.shpakovskiy.lxblog.security.EmailAndPassword;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class RegistrationChecker {

    private EmailAndPassword emailAndPassword;
    private final HttpHeaderParser httpHeaderParser;
    private final UserRepository userRepository;

    public RegistrationChecker(EmailAndPassword emailAndPassword,
                               HttpHeaderParser httpHeaderParser,
                               UserRepository userRepository) {

        this.emailAndPassword = emailAndPassword;
        this.httpHeaderParser = httpHeaderParser;
        this.userRepository = userRepository;
    }

    public User checkRegistration(HttpServletRequest request, HttpServletResponse response) {
        emailAndPassword = httpHeaderParser.getEmailAndPassword(request, response);

        if (emailAndPassword != null && userRepository.isUserAuthorized(emailAndPassword)) {
            return userRepository.getUserByEmail(emailAndPassword.getEmail());
        }
        else {
            accessDenied(response);
        }
        return null;
    }

    public void accessDenied(HttpServletResponse response) {
        try {
            response.sendRedirect("/access-denied");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}