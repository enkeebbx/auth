package com.ably.auth.service;

import com.ably.auth.model.error.AuthErrorCode;
import com.ably.auth.model.error.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;

    // 아이디, 비밀번호로 인증
    public Authentication authenticate(String username, String password) {
        try {
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            throw new AuthException(AuthErrorCode.BAD_CREDENTIALS);
        }
    }
}
