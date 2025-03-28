package com.vanrin05.model;

import com.vanrin05.domain.COUPON_TYPE;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "coupons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String code;

    double discountValue;

    LocalDateTime validityStartDate;

    LocalDateTime validityEndDate;

    double minimumOrderValue;

    boolean isActive;

    @Enumerated(EnumType.STRING)
    COUPON_TYPE couponType;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_coupons",
            joinColumns = @JoinColumn(name = "coupon_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    Set<User> usedByUsers;

    @PrePersist
    protected void prePersist() {
        this.usedByUsers = new HashSet<>();
    }
}
