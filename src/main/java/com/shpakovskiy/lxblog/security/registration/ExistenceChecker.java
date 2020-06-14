package com.shpakovskiy.lxblog.security.registration;

import com.shpakovskiy.lxblog.entity.User;
import com.shpakovskiy.lxblog.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Service
public class ExistenceChecker {

    private final UserRepository userRepository;

    public ExistenceChecker(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void checkExistence(User user, HttpServletResponse response) {
        if (Optional.ofNullable(userRepository.getUserByEmail(user.getEmail())).isPresent()) {
            userAlreadyExists(response);
        }
    }

    public void userAlreadyExists(HttpServletResponse response) {
        try {
            response.sendRedirect("/user-already-exists");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}