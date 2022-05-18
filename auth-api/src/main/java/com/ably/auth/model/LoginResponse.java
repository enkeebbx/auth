package com.ably.auth.model;

import com.ably.auth.security.service.UserDetailsImpl;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LoginResponse {

    private final String jwt;

    private final Long userId;

    private final String username;

    private final String name;

    private final String email;

    private final String phone;

    public static LoginResponse from(UserDetailsImpl userDetails, String jwt) {
        return LoginResponse.builder()
                .jwt(jwt)
                .userId(userDetails.getId())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .phone(userDetails.getPhone())
                .name(userDetails.getName())
                .build();
    }
}
