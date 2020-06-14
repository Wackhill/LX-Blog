package com.shpakovskiy.lxblog.security.registration;

import com.shpakovskiy.lxblog.entity.User;

public interface RegistrationQueueInterface {

    void addItem(User user);
    void removeFromQueue(RegistrationQueueItem user);

    String generateToken(int tokenLength);
    boolean isTokenAvailable(String token);
    User getUserByToken(String token);
    String getUsersToken(User user);
}
