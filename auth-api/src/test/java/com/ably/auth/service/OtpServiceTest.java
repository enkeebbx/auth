package com.ably.auth.service;

import com.ably.auth.client.OtpClient;
import com.ably.auth.entity.Otp;
import com.ably.auth.model.YesNo;
import com.ably.auth.model.error.AuthErrorCode;
import com.ably.auth.model.error.AuthException;
import com.ably.auth.repository.OtpRepository;
import com.ably.auth.service.factory.OtpCodeFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OtpServiceTest {

    @InjectMocks
    private OtpService sut;

    @Mock
    private OtpClient otpClient;

    @Mock
    private OtpCodeFactory otpCodeFactory;

    @Mock
    private OtpRepository otpRepository;

    @Test
    @DisplayName("OTP 저장 및 문자 발송 성공")
    void sendOtpSmsSuccess() {
        String phone = "01033002222";
        String otpCode = "641929";

        when(otpCodeFactory.generate()).thenReturn(otpCode);

        sut.sendOtpSms(phone);

        verify(otpRepository).save(any());
        verify(otpClient).sendOtpSmsToPhone(phone, otpCode);
    }

    @Test
    @DisplayName("OTP 번호 ACK 성공")
    void confirmOtpSuccess() {
        String phone = "01033002222";
        String otpCode = "641929";

        Otp otp = Otp.builder()
                .id(1L)
                .phone(phone)
                .otpCode(otpCode)
                .successYn(YesNo.N)
                .expireAt(LocalDateTime.now().plusMinutes(2))
                .build();

        when(otpRepository.findTopByPhoneAndOtpCode(phone, otpCode))
                .thenReturn(otp);

        sut.confirmOtp(phone, otpCode);

        otp.setSuccessYn(YesNo.Y);
        verify(otpRepository).save(otp);
    }

    @Test
    @DisplayName("존재하지 않는 OTP 번호, OTP 번호 ACK 실패")
    void confirmOtpNullFail() {
        String phone = "01033002222";
        String otpCode = "641929";

        when(otpRepository.findTopByPhoneAndOtpCode(phone, otpCode))
                .thenReturn(null);

        AuthException e = assertThrows(AuthException.class,
                () -> sut.confirmOtp(phone, otpCode));
        assertEquals(e.getErrorCode(), AuthErrorCode.OTP_REQUIRED.name());
    }

    @Test
    @DisplayName("만료된 OTP 번호, OTP 번호 ACK 실패")
    void confirmOtpExpiredFail() {
        String phone = "01033002222";
        String otpCode = "641929";

        Otp otp = Otp.builder()
                .id(1L)
                .phone(phone)
                .otpCode(otpCode)
                .successYn(YesNo.N)
                .expireAt(LocalDateTime.now().minusMinutes(3))
                .build();

        when(otpRepository.findTopByPhoneAndOtpCode(phone, otpCode))
                .thenReturn(null);
        when(otpRepository.findTopByPhoneAndOtpCode(phone, otpCode))
                .thenReturn(otp);

        AuthException e = assertThrows(AuthException.class,
                () -> sut.confirmOtp(phone, otpCode));
        assertEquals(e.getErrorCode(), AuthErrorCode.OTP_REQUIRED.name());
    }

    @Test
    @DisplayName("최신 OTP 번호 없음, ACK 실패")
    void validateLatestOtpNullFail() {
        String phone = "01033002222";

        when(otpRepository.findTopByPhoneOrderByIdDesc(phone))
                .thenReturn(null);

        AuthException e = assertThrows(AuthException.class,
                () -> sut.validateLatestOtpConfirm(phone));
        assertEquals(e.getErrorCode(), AuthErrorCode.OTP_REQUIRED.name());
    }

    @Test
    @DisplayName("최신 OTP 번호 만료, ACK 실패")
    void validateLatestOtpExpiredFail() {
        String phone = "01033002222";
        String otpCode = "641929";

        Otp otp = Otp.builder()
                .id(1L)
                .phone(phone)
                .otpCode(otpCode)
                .successYn(YesNo.N)
                .expireAt(LocalDateTime.now().minusMinutes(3))
                .build();

        when(otpRepository.findTopByPhoneOrderByIdDesc(phone))
                .thenReturn(otp);

        AuthException e = assertThrows(AuthException.class,
                () -> sut.validateLatestOtpConfirm(phone));
        assertEquals(e.getErrorCode(), AuthErrorCode.OTP_REQUIRED.name());
    }

    @Test
    @DisplayName("최신 OTP 번호 실패, ACK 실패")
    void validateLatestOtpFail() {
        String phone = "01033002222";
        String otpCode = "641929";

        Otp otp = Otp.builder()
                .id(1L)
                .phone(phone)
                .otpCode(otpCode)
                .successYn(YesNo.N)
                .expireAt(LocalDateTime.now().plusMinutes(2))
                .build();

        when(otpRepository.findTopByPhoneOrderByIdDesc(phone))
                .thenReturn(otp);

        AuthException e = assertThrows(AuthException.class,
                () -> sut.validateLatestOtpConfirm(phone));
        assertEquals(e.getErrorCode(), AuthErrorCode.OTP_REQUIRED.name());
    }
}
