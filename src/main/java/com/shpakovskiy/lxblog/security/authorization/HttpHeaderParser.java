package com.shpakovskiy.lxblog.security.authorization;

import com.shpakovskiy.lxblog.security.EmailAndPassword;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

@Component
public class HttpHeaderParser {

    public EmailAndPassword getEmailAndPassword(HttpServletRequest request, HttpServletResponse response) {
        String header = request.getHeader("Authorization");
        checkHeader(request, response);
        String encodedAuthData = header.substring(header.indexOf(' ') + 1);
        String authData = new String(Base64.getDecoder().decode(encodedAuthData));

        EmailAndPassword emailAndPassword = new EmailAndPassword();
        emailAndPassword.setEmail(authData.split(":")[0]);
        emailAndPassword.setPassword(authData.split(":")[1]);
        return emailAndPassword;
    }

    private void checkHeader(HttpServletRequest request, HttpServletResponse response) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.toUpperCase().contains("BASIC")) {
            noAuthData(response);
        }
    }

    private void noAuthData(HttpServletResponse response) {
        try {
            response.sendRedirect("/not-auth-data");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
