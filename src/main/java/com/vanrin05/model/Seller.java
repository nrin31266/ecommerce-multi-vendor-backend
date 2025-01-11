package com.vanrin05.model;

import com.vanrin05.domain.ACCOUNT_STATUS;
import com.vanrin05.domain.USER_ROLE;
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
    BusinessDetails businessDetails;

    @Embedded
    BankDetails bankDetails;

    @Enumerated(EnumType.STRING)
    USER_ROLE role;

    boolean isEmailVerified;

    ACCOUNT_STATUS accountStatus;

    @PrePersist
    protected void onCreate() {
        this.businessDetails = new BusinessDetails();
        this.bankDetails = new BankDetails();
        this.role = USER_ROLE.ROLE_SELLER;
        this.isEmailVerified = false;
        this.accountStatus = ACCOUNT_STATUS.PENDING_VERIFICATION;
    }

}
