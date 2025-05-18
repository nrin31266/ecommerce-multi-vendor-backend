package com.vanrin05.app.service.impl;

import com.vanrin05.app.configuration.JwtProvider;
import com.vanrin05.app.domain.ACCOUNT_STATUS;
import com.vanrin05.app.dto.request.CreateSellerRequest;
import com.vanrin05.app.dto.request.UpdateSellerRequest;
import com.vanrin05.app.exception.AppException;
import com.vanrin05.app.exception.ErrorCode;
import com.vanrin05.app.mapper.SellerMapper;
import com.vanrin05.app.model.Seller;
import com.vanrin05.app.model.SellerReport;
import com.vanrin05.app.model.VerificationCode;
import com.vanrin05.app.repository.SellerReportRepository;
import com.vanrin05.app.repository.SellerRepository;
import com.vanrin05.app.repository.VerificationCodeRepository;
import com.vanrin05.app.utils.OtpUtil;
import com.vanrin05.event.SentEmailEvent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class SellerService {
    SellerRepository sellerRepository;
    JwtProvider jwtProvider;
    PasswordEncoder passwordEncoder;
    SellerMapper sellerMapper;
    OtpUtil otpUtil;
    VerificationCodeRepository verificationCodeRepository;
    KafkaTemplate<String, Object> kafkaTemplate;
    SellerReportRepository sellerReportRepository;


    public Seller getSellerProfile(String jwt){
        String email = jwtProvider.getEmailFromJwtToken(jwt);
        return this.getSellerByEmail(email);
    }

    public Seller createSeller(CreateSellerRequest request)  {

        if(sellerRepository.findByEmail(request.getEmail()).isPresent()){
            throw new AppException("Seller already exists with email: " + request.getEmail());
        }
        Seller seller = sellerMapper.toSeller(request);
        seller.setAccountStatus(ACCOUNT_STATUS.PENDING_VERIFICATION);
        String otpCode = otpUtil.generateOtp(6);
        seller.setPassword(passwordEncoder.encode(otpCode));
        seller.setAcceptTerms(false);
        seller = sellerRepository.save(seller);

        SellerReport sellerReport = new SellerReport();
        sellerReport.setSeller(seller);
        sellerReportRepository.save(
                sellerReport
        );

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setEmail(request.getEmail());
        verificationCode.setOtp(otpCode);
        verificationCodeRepository.save(verificationCode);

        Map<String, Object> variables = new HashMap<>();
        variables.put("title", "Welcome seller to NRin, this OTP to access our System: ");
        variables.put("otp", otpCode);

        kafkaTemplate.send("sent_otp_to_login_signup", SentEmailEvent.builder()
                        .subject("Seller create account")
                        .email(seller.getEmail())
                        .variables(variables)
                .build());
        return seller;
    }

    public Seller getSellerById(Long sellerId){
        return sellerRepository.findById(sellerId).orElseThrow(() ->
                new AppException(ErrorCode.SELLER_NOT_FOUND, ErrorCode.SELLER_NOT_FOUND.getFormattedMessage("id " + sellerId)));
    }

    public Seller getSellerByEmail(String email){
        return sellerRepository.findByEmail(email).orElseThrow(() ->
                new AppException(ErrorCode.SELLER_NOT_FOUND, ErrorCode.SELLER_NOT_FOUND.getFormattedMessage("email " + email)));
    }

    public List<Seller> getAllSellers(){
        return sellerRepository.findAllByAcceptTermsIsTrue();
    }


    public Seller updateSeller(String jwt, UpdateSellerRequest request){
        Seller seller = this.getSellerProfile(jwt);
        sellerMapper.updateSeller(seller, request);
        return sellerRepository.save(seller);
    }

    public Seller accessTerms(String jwt){
        if(jwt == null){
            throw new AppException("You need login in system to accept terms");
        }
        Seller seller = this.getSellerProfile(jwt);
        seller.setAcceptTerms(true);
        return sellerRepository.save(seller);
    }

    public Seller updateSellerAccountStatus(Long sellerId, ACCOUNT_STATUS accountStatus){
        Seller seller = this.getSellerById(sellerId);
        seller.setAccountStatus(accountStatus);
        return sellerRepository.save(seller);
    }

    public void deleteSeller(Long sellerId){
        Seller seller = this.getSellerById(sellerId);
        sellerRepository.delete(seller);
    }




}
