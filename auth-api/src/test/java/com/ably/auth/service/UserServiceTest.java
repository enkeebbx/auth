package com.ably.auth.service;

import com.ably.auth.model.error.AuthException;
import com.ably.auth.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService sut;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    @DisplayName("유저 생성 성공")
    void createUserSuccess() {
        String email = "abc@gmail.com";
        String username = "abc";
        String password = "123";
        String name = "kim";
        String phone = "01033002222";

        when(userRepository.existsByEmail(email))
                .thenReturn(false);
        when(userRepository.existsByUsername(username))
                .thenReturn(false);
        when(userRepository.existsByPhone(phone))
                .thenReturn(false);
        when(passwordEncoder.encode(password))
                .thenReturn("ABC");

        sut.createUser(email, username, password, name, phone);

        verify(userRepository).save(any());
    }

    @Test
    @DisplayName("중복 이메일, 유저 생성 실패")
    void createUserEmailCheckFail() {
        String email = "abc@gmail.com";
        String username = "abc";
        String password = "123";
        String name = "kim";
        String phone = "01033002222";

        when(userRepository.existsByEmail(email))
                .thenReturn(true);

        assertThrows(AuthException.class, () ->
                sut.createUser(email, username, password, name, phone));
    }

    @Test
    @DisplayName("중복 아이디, 유저 생성 실패")
    void createUserUsernameCheckFail() {
        String email = "abc@gmail.com";
        String username = "abc";
        String password = "123";
        String name = "kim";
        String phone = "01033002222";

        when(userRepository.existsByEmail(email))
                .thenReturn(false);
        when(userRepository.existsByUsername(username))
                .thenReturn(true);

        assertThrows(AuthException.class, () ->
                sut.createUser(email, username, password, name, phone));
    }

    @Test
    @DisplayName("중복 전화번호, 유저 생성 실패")
    void createUserPhoneCheckFail() {
        String email = "abc@gmail.com";
        String username = "abc";
        String password = "123";
        String name = "kim";
        String phone = "01033002222";

        when(userRepository.existsByEmail(email))
                .thenReturn(false);
        when(userRepository.existsByUsername(username))
                .thenReturn(false);
        when(userRepository.existsByPhone(phone))
                .thenReturn(true);

        assertThrows(AuthException.class, () ->
                sut.createUser(email, username, password, name, phone));
    }
}
