package com.ably.auth.security.config;

import com.ably.auth.security.jwt.JwtEntryPoint;
import com.ably.auth.security.jwt.JwtHeaderRequestFilter;
import com.ably.auth.security.jwt.JwtUtil;
import com.ably.auth.security.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthWebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtUtil jwtUtil;

    private final UserDetailsServiceImpl userDetailsService;

    private final JwtEntryPoint jwtEntryPoint;

    @Bean
    public JwtHeaderRequestFilter authenticationJwtTokenFilter() {
        return new JwtHeaderRequestFilter(jwtUtil, userDetailsService);
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/auth/**", "/api/otp/**", "/v2/api-docs", "/swagger-resources/**", "/swagger-ui/index.html", "/swagger-ui.html", "/webjars/**", "/swagger/**")
                .permitAll()
                .antMatchers("/api/test/**")
                .permitAll()
                .anyRequest().authenticated().and();
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
