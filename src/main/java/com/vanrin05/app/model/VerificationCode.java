package com.vanrin05.app.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity(name = "verification_codes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VerificationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String otp;
    @Column(unique = true, nullable = false)
    String email;
    @OneToOne
    User user;
    @OneToOne
    Seller seller;
}
