package com.ably.auth.security.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {

    private final static List<String> BYPASS_ENDPOINTS = Arrays.asList(
            "/",
            "/csrf"
    );

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException {
        String requestUri = request.getRequestURI();
        if (!BYPASS_ENDPOINTS.contains(requestUri)) {
            log.error("서버 에러 발생: " + requestUri, ex);
        }
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "권한 없는 사용자입니다.");
    }
}
