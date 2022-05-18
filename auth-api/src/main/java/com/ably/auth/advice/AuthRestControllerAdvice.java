package com.ably.auth.advice;

import com.ably.auth.model.error.AuthErrorCode;
import com.ably.auth.model.error.AuthErrorResponse;
import com.ably.auth.model.error.AuthException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class AuthRestControllerAdvice {

    @ExceptionHandler(AuthException.class)
    @ResponseBody
    public ResponseEntity<AuthErrorResponse> handleAuthApiException(
            AuthException ex,
            HttpServletRequest request
    ) {
        log.error("서버 에러 발생: " + request.getRequestURI(), ex);
        return new ResponseEntity<>(
                ex.convertToAuthApiErrorResponse(),
                ex.getHttpStatus()
        );
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    @ResponseBody
    public ResponseEntity<AuthErrorResponse> handleInternalAuthenticationServiceException(
            InternalAuthenticationServiceException ex,
            HttpServletRequest request
    ) {
        log.error("서버 에러 발생: " + request.getRequestURI(), ex);
        AuthException authException = new AuthException(AuthErrorCode.BAD_REQUEST);
        authException.setErrorMessage(ex.getMessage());
        return new ResponseEntity<>(
                authException.convertToAuthApiErrorResponse(),
                authException.getHttpStatus()
        );
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseBody
    public ResponseEntity<AuthErrorResponse> handleMissingRequestHeaderException(
            MissingRequestHeaderException ex,
            HttpServletRequest request
    ) {
        log.error("서버 에러 발생: " + request.getRequestURI(), ex);
        AuthException authException = new AuthException(AuthErrorCode.BAD_REQUEST);
        authException.setErrorMessage(ex.getParameter() + " 필드가 필요합니다.");
        return new ResponseEntity<>(
                authException.convertToAuthApiErrorResponse(),
                authException.getHttpStatus()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<AuthErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) throws JsonProcessingException {
        log.error("서버 에러 발생: " + request.getRequestURI(), ex);
        AuthException authException = new AuthException(AuthErrorCode.BAD_REQUEST);
        authException.setErrorMessage(getErrorMessage(ex));
        return new ResponseEntity<>(
                authException.convertToAuthApiErrorResponse(),
                authException.getHttpStatus()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<AuthErrorResponse> handleException(
            Exception ex,
            HttpServletRequest request
    ) {
        log.error("알 수 없는 서버 에러 발생: " + request.getRequestURI(), ex);
        AuthException unknownServerErrorException =
                new AuthException(AuthErrorCode.UNKNOWN_SERVER_ERROR);
        return new ResponseEntity<>(
                unknownServerErrorException.convertToAuthApiErrorResponse(),
                unknownServerErrorException.getHttpStatus()
        );
    }

    // 잘못된 요청 필드 목록을 json 으로 변형
    private String getErrorMessage(MethodArgumentNotValidException ex) throws JsonProcessingException {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                .getAllErrors()
                .forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));
        return new ObjectMapper().writeValueAsString(errors);
    }
}
