package com.vanrin05.service;

import com.vanrin05.configuration.JwtProvider;
import com.vanrin05.domain.USER_ROLE;
import com.vanrin05.dto.request.SignupRequest;
import com.vanrin05.dto.response.AuthResponse;
import com.vanrin05.mapper.UserMapper;
import com.vanrin05.model.Cart;
import com.vanrin05.model.User;
import com.vanrin05.repository.CartRepository;
import com.vanrin05.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    CartRepository cartRepository;
    JwtProvider jwtProvider;

    public AuthResponse signup(SignupRequest req) {
        if(userRepository.findByEmail(req.getEmail()).isPresent()){
            throw new RuntimeException("Email already in use");
        }

        User user = userMapper.toUser(req);
        user.setRole(USER_ROLE.ROLE_CUSTOMER);
        user.setMobile("0321312321");
        user.setPassword(passwordEncoder.encode(req.getOtp()));
        user = userRepository.save(user);
        cartRepository.save(Cart.builder()
                        .user(user)
                .build());

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().toString()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return AuthResponse.builder()
                .jwt(jwtProvider.generateToken(authentication))
                .message("Registered Successfully")
                .role(user.getRole())
                .build();
    }


}
