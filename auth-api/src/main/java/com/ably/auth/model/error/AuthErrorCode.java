package com.ably.auth.model.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode {

    EMAIL_ALREADY_EXIST("중복된 이메일 주소입니다.", HttpStatus.BAD_REQUEST),
    USERNAME_ALREADY_EXIST("중복된 아이디입니다.", HttpStatus.BAD_REQUEST),
    PHONE_ALREADY_EXIST("중복된 전화번호입니다.", HttpStatus.BAD_REQUEST),
    USER_NOT_EXIST("없는 유저입니다.", HttpStatus.BAD_REQUEST),
    JWT_REQUIRED("인증토큰을 확인해주세요.", HttpStatus.BAD_REQUEST),
    BAD_REQUEST("잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    BAD_CREDENTIALS("아이디(이메일/전화번호)와 비밀번호를 다시 확인해주세요.", HttpStatus.UNAUTHORIZED),
    OTP_REQUIRED("전화번호 인증이 필요합니다.", HttpStatus.UNAUTHORIZED),
    UNKNOWN_SERVER_ERROR("알 수 없는 서버 에러입니다. 로그를 확인해주세요.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String errorMessage;

    private final HttpStatus httpStatus;
}
