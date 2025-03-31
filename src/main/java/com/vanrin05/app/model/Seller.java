package com.vanrin05.app.model;

import com.vanrin05.app.domain.ACCOUNT_STATUS;
import com.vanrin05.app.domain.USER_ROLE;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity(name = "sellers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String sellerName;
    String mobile;
    @Column(unique = true, nullable = false)
    String email;
    String password;
    @Embedded
    BusinessDetails businessDetails = new BusinessDetails();
    @Embedded
    BankDetails bankDetails = new BankDetails();
    @Enumerated(EnumType.STRING)
    USER_ROLE role;
    boolean isEmailVerified;
    @Enumerated(EnumType.STRING)
    ACCOUNT_STATUS accountStatus;
    String taxCode;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    Address pickupAddress;
    String gstin;
    @PrePersist
    protected void onCreate() {
        this.role = USER_ROLE.ROLE_SELLER;
//        this.isEmailVerified = false;
        this.accountStatus = ACCOUNT_STATUS.PENDING_VERIFICATION;
    }

}
