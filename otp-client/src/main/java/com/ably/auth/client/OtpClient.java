package com.ably.auth.client;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class OtpClient {

    // 전화번호로 OTP 인증번호 문자 발송 (비동기)
    @Async
    public void sendOtpSmsToPhone(String phone, String otp) {
        // TODO: 제3파티 전화번호 인증 API 호출
    }
}
