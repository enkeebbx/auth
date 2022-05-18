package com.ably.auth.security.service;

import com.ably.auth.entity.User;
import com.ably.auth.model.error.AuthErrorCode;
import com.ably.auth.model.error.AuthException;
import com.ably.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.getUser(username);
        if (user == null) {
            throw new AuthException(AuthErrorCode.USER_NOT_EXIST);
        }
        return UserDetailsImpl.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .phone(user.getPhone())
                .name(user.getName())
                .build();
    }
}
