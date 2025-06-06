package com.vanrin05.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@EqualsAndHashCode(exclude = {"pickupAddress"})
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String sellerName;
    String mobile;
    @Column(unique = true, nullable = false)
    String email;
    @JsonIgnore
    String password;
    @Embedded
    BusinessDetails businessDetails = new BusinessDetails();
    @Embedded
    BankDetails bankDetails = new BankDetails();
    @Enumerated(EnumType.STRING)
    USER_ROLE role;
    Boolean acceptTerms;
    @Enumerated(EnumType.STRING)
    ACCOUNT_STATUS accountStatus;
    String taxCode;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "pickup_address_id")
    Address pickupAddress;
    String gstin;
    @PrePersist
    protected void onCreate() {
        this.role = USER_ROLE.ROLE_SELLER;
//        this.isEmailVerified = false;
        this.accountStatus = ACCOUNT_STATUS.PENDING_VERIFICATION;
    }

}
