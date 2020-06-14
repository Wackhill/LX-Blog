package com.shpakovskiy.lxblog.repository;

import com.shpakovskiy.lxblog.entity.User;
import com.shpakovskiy.lxblog.security.EmailAndPassword;

import java.util.List;

public interface UserRepositoryInterface {

    List<User> getAllUsers();

    User getUserById(int id);
    User getUserByEmail(String email);

    void addUser(User user);
    void updateUser(User user);

    boolean isUserExist(User user);
    boolean isUserAuthorized(EmailAndPassword emailAndPassword);
}