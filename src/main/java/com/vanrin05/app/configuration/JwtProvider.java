package com.vanrin05.app.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;


import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Slf4j
public class JwtProvider {
    @Value("${security.jwt.secretKey}")
    private String SECRET_KEY;

    private SecretKey secretKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(Authentication auth){

        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        String roles = populateAuthorities(authorities);

        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(Instant.now().plus(24, ChronoUnit.HOURS).toEpochMilli()))
                .claim("email", auth.getName())
                .claim("authorities", roles)
                .signWith(secretKey())
                .compact();
    }
    public Claims validateToken(String jwtToken) {
        return parseToken(jwtToken); // Sử dụng parseToken
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> auths = new HashSet<>();
        for (GrantedAuthority authority : authorities) {
            auths.add(authority.getAuthority());
        }
        return String.join(",", auths);
    }

    public String getEmailFromJwtToken(String jwtToken) {
        Claims claims = parseToken(jwtToken);
        return String.valueOf(claims.get("email"));
    }

    public Claims parseToken(String jwtToken) {
        jwtToken = jwtToken.substring(7); // Xử lý "Bearer "
        SecretKey key = secretKey();
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(jwtToken).getBody();
    }


}
