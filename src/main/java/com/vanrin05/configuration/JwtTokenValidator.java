package com.vanrin05.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

@Slf4j
public class JwtTokenValidator extends OncePerRequestFilter {


    private final String SECRET_KEY;
    private final String HEADER;

    public JwtTokenValidator(String secretKey, String header) {
        this.SECRET_KEY = secretKey;
        this.HEADER = header;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = request.getHeader("Authorization");
        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            jwtToken = jwtToken.substring(7);
            try{
                SecretKey secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
                Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build()
                        .parseClaimsJws(jwtToken).getBody();

                String email = String.valueOf(claims.get("email"));
                String authorities = String.valueOf(claims.get("authorities"));


                List<GrantedAuthority> authorityList = AuthorityUtils
                        .commaSeparatedStringToAuthorityList(authorities);

                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null ,authorityList);

                SecurityContextHolder.getContext().setAuthentication(authentication);

            }catch (Exception e) {
                throw new BadCredentialsException("Invalid JWT token...");
            }
        }
        log.info("Url: "+request.getRequestURL());

        filterChain.doFilter(request, response);
    }
}
