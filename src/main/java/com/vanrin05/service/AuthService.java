package com.vanrin05.service;

import com.vanrin05.configuration.JwtProvider;
import com.vanrin05.domain.USER_ROLE;
import com.vanrin05.dto.request.SigningRequest;
import com.vanrin05.dto.request.SignupRequest;
import com.vanrin05.dto.response.AuthResponse;
import com.vanrin05.mapper.UserMapper;
import com.vanrin05.model.Cart;
import com.vanrin05.model.Seller;
import com.vanrin05.model.User;
import com.vanrin05.model.VerificationCode;
import com.vanrin05.repository.CartRepository;
import com.vanrin05.repository.SellerRepository;
import com.vanrin05.repository.UserRepository;
import com.vanrin05.repository.VerificationCodeRepository;
import com.vanrin05.utils.OtpUtil;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

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
    EmailService emailService;
    CustomUserServiceImpl customUserService;
    SellerRepository sellerRepository;

    public void sendLoginOtp (String email, USER_ROLE role) throws MessagingException {


        String SINGING_PREFIX = "signing_";

        if(email.startsWith(SINGING_PREFIX)){
            email = email.substring(SINGING_PREFIX.length());
            log.info("Email: "+email);
            if(role.equals(USER_ROLE.ROLE_SELLER)){
                if(sellerRepository.findByEmail(email).isEmpty()){
                    throw new BadCredentialsException("Seller not found with email: " + email);
                }
            }else{
                if(userRepository.findByEmail(email).isEmpty()){
                    throw new RuntimeException(("User not found with email: " + email));
                }
            }
        }else{
            if(userRepository.findByEmail(email).isPresent()){
                throw new RuntimeException(("Email already in use: " + email));
            }
        }

        VerificationCode verificationCode = new VerificationCode();
        Optional<VerificationCode> verificationCodeOptional = verificationCodeRepository.findByEmail(email);
        if(verificationCodeOptional.isPresent()){
            verificationCode = verificationCodeOptional.get();
        }


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

        if (verificationCodeOptional.isEmpty() || !verificationCodeOptional.get().getOtp().equals(req.getOtp())) {
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
            throw new BadCredentialsException("Wrong verification otp");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null,  userDetails.getAuthorities());
    }
}
