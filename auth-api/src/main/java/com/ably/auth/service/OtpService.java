package com.ably.auth.service;

import com.ably.auth.client.OtpClient;
import com.ably.auth.entity.Otp;
import com.ably.auth.model.error.AuthErrorCode;
import com.ably.auth.model.error.AuthException;
import com.ably.auth.repository.OtpRepository;
import com.ably.auth.service.factory.OtpCodeFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpClient otpClient;

    private final OtpCodeFactory otpCodeFactory;

    private final OtpRepository otpRepository;

    @Value("${otp.ttl-min}")
    private int otpTtlMin;

    // OTP 저장 및 문자 발송
    public String sendOtpSms(String phone) {
        String otpCode = otpCodeFactory.generate();
        otpRepository.save(Otp.of(phone, otpCode, otpTtlMin));
        otpClient.sendOtpSmsToPhone(phone, otpCode);
        return otpCode;
    }

    // OTP 번호 ACK
    public void confirmOtp(String phone, String otpCode) {
        Otp otp = otpRepository.findTopByPhoneAndOtpCode(phone, otpCode);
        if (otp == null || otp.isExpired()) {
            throw new AuthException(AuthErrorCode.OTP_REQUIRED);
        } else {
            otp.success();
            otpRepository.save(otp);
        }
    }

    // 최신 OTP 번호 ACK 여부 확인
    public void validateLatestOtpConfirm(String phone) {
        Otp latestOtp = otpRepository.findTopByPhoneOrderByIdDesc(phone);
        if (latestOtp == null || latestOtp.isExpired() || latestOtp.isFail()) {
            throw new AuthException(AuthErrorCode.OTP_REQUIRED);
        }
    }
}
