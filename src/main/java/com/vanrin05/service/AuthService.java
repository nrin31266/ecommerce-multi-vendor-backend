package com.vanrin05.service;

import com.vanrin05.configuration.JwtProvider;
import com.vanrin05.domain.USER_ROLE;
import com.vanrin05.dto.request.SignupRequest;
import com.vanrin05.dto.response.AuthResponse;
import com.vanrin05.mapper.UserMapper;
import com.vanrin05.model.Cart;
import com.vanrin05.model.User;
import com.vanrin05.model.VerificationCode;
import com.vanrin05.repository.CartRepository;
import com.vanrin05.repository.UserRepository;
import com.vanrin05.repository.VerificationCodeRepository;
import com.vanrin05.utils.OtpUtil;
import jakarta.mail.MessagingException;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    CartRepository cartRepository;
    JwtProvider jwtProvider;
    VerificationCodeRepository verificationCodeRepository;
    OtpUtil otpUtil;
    EmailService emailService;

    public void sendLoginOtp (String email) throws MessagingException {


        String SINGING_PREFIX = "singing_";
        if(email.startsWith(SINGING_PREFIX)){
            email = email.substring(SINGING_PREFIX.length());
            if(userRepository.findByEmail(email).isEmpty()){
                throw new RuntimeException(("User not exist with email: " + email));
            }
        }else{
            if(userRepository.findByEmail(email).isPresent()){
                throw new RuntimeException(("Email already in use: " + email));
            }
        }

        Optional<VerificationCode> verificationCodeOptional = verificationCodeRepository.findByEmail(email);
        verificationCodeOptional.ifPresent(verificationCodeRepository::delete);

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setEmail(email);
        verificationCode.setOtp(otpUtil.generateOtp(6));
        verificationCodeRepository.save(verificationCode);

        String subject = "Ecommerce multi vendor login/signup OTP";
        String text = "Your login/signup OTP is " + verificationCode.getOtp();

        emailService.sendVerificationOtpEmail(email, subject, text);
    }


    public AuthResponse signup(SignupRequest req) {
        if(userRepository.findByEmail(req.getEmail()).isPresent()){
            throw new RuntimeException("Email already in use");
        }

        Optional<VerificationCode> verificationCodeOptional = verificationCodeRepository.findByEmail(req.getEmail());

        if (verificationCodeOptional.isEmpty() || !verificationCodeOptional.get().equals(req.getOtp())) {
            throw new RuntimeException("Wrong otp...");

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
