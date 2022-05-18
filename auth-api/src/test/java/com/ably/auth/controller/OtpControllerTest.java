package com.ably.auth.controller;

import com.ably.auth.security.jwt.JwtEntryPoint;
import com.ably.auth.security.jwt.JwtUtil;
import com.ably.auth.security.service.UserDetailsServiceImpl;
import com.ably.auth.service.OtpService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(OtpController.class)
public class OtpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OtpService otpService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JwtEntryPoint jwtEntryPoint;

    @Test
    @DisplayName("OTP 문자전송 테스트, 200 성공")
    void sendOtpSms200Test() throws Exception {
        when(otpService.sendOtpSms(any()))
                .thenReturn("123232");
        mockMvc.perform(post("/api/otp/sms")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"phone\":\"01092923232\"}"))
                .andExpect(status().is(200));
    }

    @Test
    @DisplayName("OTP 문자전송 테스트, 전화번호 필드누락 400 실패")
    void sendOtpSms400Test() throws Exception {
        when(otpService.sendOtpSms(any()))
                .thenReturn("123232");
        mockMvc.perform(post("/api/otp/sms")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"phone\":\"\"}"))
                .andExpect(status().is(400));
    }

    @Test
    @DisplayName("OTP 컨펌 테스트, 200 성공")
    void confirmOtp200Test() throws Exception {
        String otpCode = "123232";
        when(otpService.sendOtpSms(any()))
                .thenReturn(otpCode);
        mockMvc.perform(post("/api/otp/sms")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"phone\":\"01092923232\",\"otp_number\":\"" + otpCode + "\"}"))
                .andExpect(status().is(200));
    }

    @Test
    @DisplayName("OTP 컨펌 테스트, 전화번호 필드누락 400 실패")
    void confirmOtp400Test() throws Exception {
        String otpCode = "123232";
        when(otpService.sendOtpSms(any()))
                .thenReturn(otpCode);
        mockMvc.perform(post("/api/otp/sms")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"phone\":\"\",\"otp_number\":\"" + otpCode + "\"}"))
                .andExpect(status().is(400));
    }
}
