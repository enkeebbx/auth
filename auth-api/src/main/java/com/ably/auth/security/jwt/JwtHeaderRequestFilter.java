package com.ably.auth.security.jwt;

import com.ably.auth.model.Constants;
import com.ably.auth.security.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtHeaderRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private final UserDetailsServiceImpl userDetailsService;

    private final static int JWT_POSITION = 7;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String jwt = getJwt(request);
        if (jwt != null) {
            applyUserDetailsWithJwt(request, jwt);
        }
        filterChain.doFilter(request, response);
    }

    private void applyUserDetailsWithJwt(HttpServletRequest request, String jwt) {
        String username = jwtUtil.getUsername(jwt);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String getJwt(HttpServletRequest request) {
        String header = request.getHeader(Constants.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(header) && header.startsWith(Constants.BEARER_HEADER)) {
            String jwt = header.substring(JWT_POSITION);
            jwtUtil.validateJwt(jwt);
            return jwt;
        }
        return null;
    }
}
