package com.ably.auth.controller;

import com.ably.auth.security.jwt.JwtEntryPoint;
import com.ably.auth.security.jwt.JwtUtil;
import com.ably.auth.security.service.UserDetailsImpl;
import com.ably.auth.security.service.UserDetailsServiceImpl;
import com.ably.auth.service.AuthService;
import com.ably.auth.service.OtpService;
import com.ably.auth.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private UserService userService;

    @MockBean
    private OtpService otpService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JwtEntryPoint jwtEntryPoint;

    @Mock
    private Authentication authentication;

    @Test
    @DisplayName("회원가입 성공")
    void registerPost200Test() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"abc@gmail.com\",\"username\":\"kim\",\"password\":\"123\",\"name\":\"kim\",\"phone\":\"01033119222\"}"))
                .andExpect(status().is(200));
    }

    @Test
    @DisplayName("회원가입 실패, 아이디 필드 누락")
    void registerPostFail400Test() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"abc@gmail.com\",\"password\":\"123\",\"name\":\"kim\",\"phone\":\"01033119222\"}"))
                .andExpect(status().is(400));
    }

    @Test
    @DisplayName("로그인 성공")
    void loginPost200Test() throws Exception {
        String key = "abc@gmail.com";
        String password = "123";
        String jwt = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJybGFkYnNkbmQiLCJpYXQiOjE2NTI4Mjk1MjYsImV4cCI6MTY1MjkxNTkyNn0.7NRXCCxk9e1Z40yL3IqowM3ePpgfGu2tgy1K7iXfdS3fmCDplN_SGdUKxRuXbI0s6_OvgH9mODuq1HLzN7m-VA";

        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .name("kim")
                .email(key)
                .password(password)
                .phone("01033393333")
                .username("kim")
                .build();

        when(authService.authenticate(key, password))
                .thenReturn(authentication);
        when(authentication.getPrincipal())
                .thenReturn(userDetails);
        when(jwtUtil.generateJwt(authentication))
                .thenReturn(jwt);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"key\":\"abc@gmail.com\",\"password\":\"123\"}"))
                .andExpect(status().is(200));
    }

    @Test
    @DisplayName("로그인 실패, 비밀번호 누락")
    void loginPost400Test() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"key\":\"abc@gmail.com\"}"))
                .andExpect(status().is(400));
    }


    @Test
    @DisplayName("프로필 조회 성공")
    void getUserProfile200Test() throws Exception {
        String key = "abc@gmail.com";
        String password = "123";
        String jwt = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJybGFkYnNkbmQiLCJpYXQiOjE2NTI4Mjk1MjYsImV4cCI6MTY1MjkxNTkyNn0.7NRXCCxk9e1Z40yL3IqowM3ePpgfGu2tgy1K7iXfdS3fmCDplN_SGdUKxRuXbI0s6_OvgH9mODuq1HLzN7m-VA";

        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .name("kim")
                .email(key)
                .password(password)
                .phone("01033393333")
                .username("kim")
                .build();

        when(jwtUtil.getUsername(jwt))
                .thenReturn(key);
        when(userDetailsService.loadUserByUsername(key))
                .thenReturn(userDetails);

        mockMvc.perform(get("/api/auth/profile")
                .header("Authorization", "Bearer " + jwt))
                .andExpect(status().is(200));
    }

    @Test
    @DisplayName("프로필 조회 실패, 인증토큰 없음")
    void getUserProfile400Test() throws Exception {
        mockMvc.perform(get("/api/auth/profile"))
                .andExpect(status().is(400));
    }

    @Test
    @DisplayName("비밀번호 변경 성공")
    void updatedPasswordPost200Test() throws Exception {
        mockMvc.perform(post("/api/auth/updatepw")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"phone\":\"01022323222\",\"new_password\":\"123\"}"))
                .andExpect(status().is(200));
    }

    @Test
    @DisplayName("비밀번호 변경 실패, 비밀번호 필드 누락")
    void updatedPasswordPost400Test() throws Exception {
        mockMvc.perform(post("/api/auth/updatepw")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"phone\":\"01022323222\"}"))
                .andExpect(status().is(400));
    }
}
