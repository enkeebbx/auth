package com.ably.auth.service.factory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OtpCodeFactoryTest {

    private final OtpCodeFactory sut = new OtpCodeFactory();

    @Test
    @DisplayName("OTP 코드 6자리 생성")
    void generateSixDigitOtpCode() {
        String otpCode = sut.generate();
        assertEquals(otpCode.length(), 6);
    }
}
