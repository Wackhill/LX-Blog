package com.shpakovskiy.lxblog.security.registration;

import com.shpakovskiy.lxblog.entity.User;
import com.shpakovskiy.lxblog.repository.UserRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class TokenValidator {

    private final RegistrationQueueService registrationQueueService;
    private final UserRepository userRepository;

    public TokenValidator(RegistrationQueueService registrationQueueService,
                          UserRepository userRepository) {

        this.registrationQueueService = registrationQueueService;
        this.userRepository = userRepository;
    }

    public void validateToken(String token, HttpServletResponse response) {
        Optional<User> userInQueue =
                Optional.ofNullable(registrationQueueService.getUserByToken(token));

        if (userInQueue.isPresent()) {
            userRepository.addUser(userInQueue.get());
        }
        else {
            noSuchToken(response);
        }
    }

    private void noSuchToken(HttpServletResponse response) {
        try {
            response.sendRedirect("/no-such-token");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
