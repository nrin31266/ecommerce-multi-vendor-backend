package com.vanrin05.app.service.impl;

import com.vanrin05.app.configuration.JwtProvider;
import com.vanrin05.app.domain.ACCOUNT_STATUS;
import com.vanrin05.app.dto.request.CreateSellerRequest;
import com.vanrin05.app.dto.request.UpdateSellerRequest;
import com.vanrin05.app.exception.AppException;
import com.vanrin05.app.exception.ErrorCode;
import com.vanrin05.app.mapper.SellerMapper;
import com.vanrin05.app.model.Seller;
import com.vanrin05.app.model.VerificationCode;
import com.vanrin05.app.repository.AddressRepository;
import com.vanrin05.app.repository.SellerRepository;
import com.vanrin05.app.repository.VerificationCodeRepository;
import com.vanrin05.app.utils.OtpUtil;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class SellerService {
    SellerRepository sellerRepository;
    JwtProvider jwtProvider;
    PasswordEncoder passwordEncoder;
    AddressRepository addressRepository;
    SellerMapper sellerMapper;
    EmailService emailService;
    OtpUtil otpUtil;
    VerificationCodeRepository verificationCodeRepository;

    public List<Seller> getSellersByIds(Set<Long> ids) {
        return sellerRepository.findAllByIdIn(ids);
    }
    public Seller getSellerProfile(String jwt){
        String email = jwtProvider.getEmailFromJwtToken(jwt);


        return this.getSellerByEmail(email);
    }

    public Seller createSeller(CreateSellerRequest request) throws MessagingException {

        if(sellerRepository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Seller already exists with email: " + request.getEmail());
        }
        Seller seller = sellerMapper.toSeller(request);
        log.info(seller.toString());
        seller = sellerRepository.save(seller);
//        VerificationCode verificationCode = new VerificationCode();
//        verificationCode.setEmail(request.getEmail());
//        String otpCode = otpUtil.generateOtp(6);
//        verificationCode.setOtp(otpCode);
//        verificationCodeRepository.save(verificationCode);
        String subject = "Ecommerce Multi Vendor - Email Verification Code Of Seller";
        String body = "Well come to EcommerceMV, verify account using link " +
                      "http://localhost:3000/verify-seller/";
        emailService.sendVerificationOtpEmail(seller.getEmail(), subject, body);
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

    public List<Seller> getAllSellers(ACCOUNT_STATUS accountStatus){
        return sellerRepository.findByAccountStatus(accountStatus);
    }


    public Seller updateSeller(String jwt, UpdateSellerRequest request){
        Seller seller = this.getSellerProfile(jwt);
        sellerMapper.updateSeller(seller, request);

        return sellerRepository.save(seller);
    }

    public Seller verifyEmail(String jwt, String otp){
        Seller seller = this.getSellerProfile(jwt);
        Optional<VerificationCode> verificationCodeOptional = verificationCodeRepository.findByEmail(seller.getEmail());

        if(verificationCodeOptional.isEmpty() || !verificationCodeOptional.get().getOtp().equals(otp)){
            throw new RuntimeException("Wrong otp");
        }

        seller.setEmailVerified(true);
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
