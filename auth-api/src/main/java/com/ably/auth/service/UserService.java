package com.ably.auth.service;

import com.ably.auth.entity.User;
import com.ably.auth.model.error.AuthErrorCode;
import com.ably.auth.model.error.AuthException;
import com.ably.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    // 이메일, 아이디, 전화번호 중복체크 이후 유저 생성
    public void createUser(
            String email,
            String username,
            String password,
            String name,
            String phone
    ) {
        checkUniqueEmail(email);
        checkUniqueUsername(username);
        checkUniquePhone(phone);
        userRepository.save(User.builder()
                .email(email)
                .username(username)
                .password(passwordEncoder.encode(password))
                .name(name)
                .phone(phone)
                .build()
        );
    }

    // 전화번호로 유저 조회 이후 비밀번호 변경
    public void updateUser(String phone, String password) {
        User user = userRepository.findByPhone(phone);
        if (user == null) {
            throw new AuthException(AuthErrorCode.USER_NOT_EXIST);
        }
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    // 이메일 중복체크
    private void checkUniqueEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new AuthException(AuthErrorCode.EMAIL_ALREADY_EXIST);
        }
    }

    // 아이디 중복체크
    private void checkUniqueUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new AuthException(AuthErrorCode.USERNAME_ALREADY_EXIST);
        }
    }

    // 전화번호 중복체크
    private void checkUniquePhone(String phone) {
        if (userRepository.existsByPhone(phone)) {
            throw new AuthException(AuthErrorCode.PHONE_ALREADY_EXIST);
        }
    }
}
