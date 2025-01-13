package com.vanrin05.repository;

import com.vanrin05.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationCodeRepository  extends JpaRepository<VerificationCode, Long> {
    Optional<VerificationCode> findByEmail(String email);
}
