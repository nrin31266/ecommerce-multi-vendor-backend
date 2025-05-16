package com.vanrin05.app.service.impl;

import com.vanrin05.app.configuration.JwtProvider;
import com.vanrin05.app.domain.USER_ROLE;
import com.vanrin05.app.dto.request.SigningRequest;
import com.vanrin05.app.dto.request.SignupRequest;
import com.vanrin05.app.dto.response.AuthResponse;
import com.vanrin05.app.dto.response.VerifyTokenResponse;
import com.vanrin05.app.exception.AppException;
import com.vanrin05.app.exception.ErrorCode;
import com.vanrin05.app.mapper.UserMapper;
import com.vanrin05.app.model.cart.Cart;
import com.vanrin05.app.model.User;
import com.vanrin05.app.model.VerificationCode;
import com.vanrin05.app.repository.CartRepository;
import com.vanrin05.app.repository.SellerRepository;
import com.vanrin05.app.repository.UserRepository;
import com.vanrin05.app.repository.VerificationCodeRepository;
import com.vanrin05.app.utils.OtpUtil;
import com.vanrin05.event.SentLoginSignupEvent;
import io.jsonwebtoken.Claims;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
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
    CustomUserServiceImpl customUserService;
    SellerRepository sellerRepository;
    KafkaTemplate<String, Object> kafkaTemplate;

    public void sendLoginOtp (String email, USER_ROLE role) throws MessagingException {
        SentLoginSignupEvent sentLoginSignupEvent = new SentLoginSignupEvent();
        Map<String, Object> variables = new HashMap<>();

        String SINGING_PREFIX = "signing_";

        if(email.startsWith(SINGING_PREFIX)){
            email = email.substring(SINGING_PREFIX.length());

            if(role.equals(USER_ROLE.ROLE_SELLER)){
                if(sellerRepository.findByEmail(email).isEmpty()){
                    throw new AppException("Seller not found with email: " + email);
                }

            }else{
                if(userRepository.findByEmail(email).isEmpty()){
                    throw new AppException(("User not found with email: " + email));
                }
            }
            sentLoginSignupEvent.setSubject("Ecommerce MV: Login OTP");
            variables.put("title", "Welcome back Ecommerce MV");
        }else{
            if(userRepository.findByEmail(email).isPresent()){
                throw new AppException(("Email already in use: " + email));
            }
            sentLoginSignupEvent.setSubject("Ecommerce MV: Signup OTP");
            variables.put("title", "Welcome to to Ecommerce MV");
        }

        VerificationCode verificationCode = new VerificationCode();
        Optional<VerificationCode> verificationCodeOptional = verificationCodeRepository.findByEmail(email);
        if(verificationCodeOptional.isPresent()){
            verificationCode = verificationCodeOptional.get();
        }


        verificationCode.setEmail(email);
        verificationCode.setOtp(otpUtil.generateOtp(6));
        verificationCodeRepository.save(verificationCode);

        variables.put("otp", verificationCode.getOtp());
        sentLoginSignupEvent.setVariables(variables);
        sentLoginSignupEvent.setEmail(email);
        kafkaTemplate.send("sent_otp_to_login_signup", sentLoginSignupEvent);

    }


    public AuthResponse signup(SignupRequest req) {
        if(userRepository.findByEmail(req.getEmail()).isPresent()){
            throw new AppException("Email already in use");
        }

        Optional<VerificationCode> verificationCodeOptional = verificationCodeRepository.findByEmail(req.getEmail());

        if (verificationCodeOptional.isEmpty() || !verificationCodeOptional.get().getOtp().equals(req.getOtp())) {
            throw new AppException("Wrong otp...");
        }



        User user = userMapper.toUser(req);
        user.setRole(USER_ROLE.ROLE_CUSTOMER);
        user.setMobile("0321312321");
        user.setPassword(
                passwordEncoder.encode(verificationCodeOptional.get().getOtp())
        );
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


    public AuthResponse signing(SigningRequest req) {


        Authentication authentication = getAuthentication(req.getEmail(), req.getOtp());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roleName = authorities.isEmpty()?null:authorities.iterator().next().getAuthority();



        return AuthResponse.builder()
                .role(USER_ROLE.valueOf(roleName))
                .message("Signed Successfully")
                .jwt(jwtProvider.generateToken(authentication))
                .build();

    }

    private Authentication getAuthentication(String username, String otp) {
        UserDetails userDetails = customUserService.loadUserByUsername(username);
        if(userDetails == null){
            throw new BadCredentialsException("Invalid username");
        }
        VerificationCode verificationCode = verificationCodeRepository.findByEmail(userDetails.getUsername()).orElse(null);

        if(verificationCode == null || !verificationCode.getOtp().equals(otp)){
            throw new AppException(ErrorCode.WRONG_OTP);
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null,  userDetails.getAuthorities());
    }

    public VerifyTokenResponse verifyToken(String token) {
        boolean isValid;
        String details;
        try {
            Claims claims = jwtProvider.validateToken(token);
            isValid = true;
            details = claims.get("email").toString();
        }catch (Exception e){
            isValid = false;
            details = e.getMessage();
        }

        return new VerifyTokenResponse(
                isValid, details
        );
    }
}
