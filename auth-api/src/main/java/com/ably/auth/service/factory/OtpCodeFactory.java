package com.ably.auth.service.factory;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class OtpCodeFactory {

    private final static int SIX_DIGIT = 6;

    private final static int MIN = 0;

    private final static int MAX = 999999;

    public String generate() {
        int randomInt = ThreadLocalRandom.current()
                .nextInt(MIN, MAX);
        return String.format("%" + SIX_DIGIT + "s", randomInt)
                .replace(' ', '0');
    }
}
