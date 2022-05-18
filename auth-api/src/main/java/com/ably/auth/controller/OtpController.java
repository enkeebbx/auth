package com.ably.auth.controller;

import com.ably.auth.model.OtpConfirmRequest;
import com.ably.auth.model.OtpSmsRequest;
import com.ably.auth.model.OtpSmsResponse;
import com.ably.auth.service.OtpService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Api(tags = "OtpController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/otp")
public class OtpController {

    private final OtpService otpService;

    @ApiOperation(value = "OTP SMS 전송")
    @PostMapping("/sms")
    public OtpSmsResponse sendOtpSms(@Valid @RequestBody OtpSmsRequest otpSmsRequest) {
        return OtpSmsResponse.of(otpService.sendOtpSms(otpSmsRequest.getPhone()));
    }

    @ApiOperation(value = "OTP SMS ACK")
    @PostMapping("/confirm")
    public void confirmOtp(@RequestBody OtpConfirmRequest otpConfirmRequest) {
        otpService.confirmOtp(
                otpConfirmRequest.getPhone(),
                otpConfirmRequest.getOtpNumber()
        );
    }

}
