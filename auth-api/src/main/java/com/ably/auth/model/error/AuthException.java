package com.ably.auth.model.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AuthException extends RuntimeException {

    private String errorCode;

    private String errorMessage;

    private HttpStatus httpStatus;

    public AuthException(AuthErrorCode authErrorCode) {
        super(authErrorCode.getErrorMessage());
        this.errorCode = authErrorCode.name();
        this.errorMessage = authErrorCode.getErrorMessage();
        this.httpStatus = authErrorCode.getHttpStatus();
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public AuthErrorResponse convertToAuthApiErrorResponse() {
        return AuthErrorResponse.builder()
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .build();
    }
}
