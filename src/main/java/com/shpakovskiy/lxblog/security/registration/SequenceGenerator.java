package com.shpakovskiy.lxblog.security.registration;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class SequenceGenerator {

    public String generateToken(int length) {
        StringBuilder sequenceBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sequenceBuilder.append(((char) (new Random().nextInt('z' - 'a' + 1) + 'a')));
        }
        return sequenceBuilder.toString();
    }
}
