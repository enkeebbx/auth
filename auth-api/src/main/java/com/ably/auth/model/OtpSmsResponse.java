package com.ably.auth.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OtpSmsResponse {

    private final String otpCode;

    public static OtpSmsResponse of(String otpCode) {
        return OtpSmsResponse.builder()
                .otpCode(otpCode)
                .build();
    }
}
