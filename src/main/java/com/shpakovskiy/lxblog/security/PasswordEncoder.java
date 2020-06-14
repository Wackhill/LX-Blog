package com.shpakovskiy.lxblog.security;

import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

@Component
public class PasswordEncoder {

    public String encode(String plainPassword) {
        return DigestUtils.md5DigestAsHex(plainPassword.getBytes(StandardCharsets.UTF_8));
    }
}
