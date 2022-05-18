package com.ably.auth.controller;

import com.ably.auth.model.*;
import com.ably.auth.model.error.AuthErrorCode;
import com.ably.auth.model.error.AuthException;
import com.ably.auth.security.jwt.JwtUtil;
import com.ably.auth.security.service.UserDetailsImpl;
import com.ably.auth.service.AuthService;
import com.ably.auth.service.OtpService;
import com.ably.auth.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "AuthController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    private final UserService userService;

    private final OtpService otpService;

    private final JwtUtil jwtUtil;

    @ApiOperation(value = "OTP 인증 이후 가입")
    @PostMapping("/register")
    public void register(@Valid @RequestBody RegisterRequest registerRequest) {
        otpService.validateLatestOtpConfirm(registerRequest.getPhone());
        userService.createUser(
                registerRequest.getEmail(),
                registerRequest.getUsername(),
                registerRequest.getPassword(),
                registerRequest.getName(),
                registerRequest.getPhone()
        );
    }

    @ApiOperation(value = "로그인")
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authService.authenticate(
                loginRequest.getKey(),
                loginRequest.getPassword()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateJwt(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return LoginResponse.from(userDetails, jwt);
    }

    @ApiOperation(value = "프로필 조회")
    @GetMapping("/profile")
    public UserProfile getUserProfile(@RequestHeader(Constants.AUTHORIZATION_HEADER) String authorization) {
        if (!authorization.startsWith(Constants.BEARER_HEADER)) {
            throw new AuthException(AuthErrorCode.JWT_REQUIRED);
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return UserProfile.from(userDetails);
    }

    @ApiOperation(value = "OTP 인증 이후 비밀번호 재설정")
    @PostMapping("/updatepw")
    public void updatePassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        otpService.validateLatestOtpConfirm(updatePasswordRequest.getPhone());
        userService.updateUser(
                updatePasswordRequest.getPhone(),
                updatePasswordRequest.getNewPassword()
        );
    }
}
