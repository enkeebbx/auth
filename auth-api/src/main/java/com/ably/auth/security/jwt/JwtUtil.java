package com.ably.auth.security.jwt;

import com.ably.auth.security.service.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret-key}")
    private String jwtSecret;

    @Value("${jwt.expire-ms}")
    private int jwtExpireMs;

    // 이메일/아아디/전화번호로 jwt 생성
    public String generateJwt(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpireMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    // jwt 내용 추출
    public String getUsername(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public void validateJwt(String jwt) {
        Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt);
    }
}
