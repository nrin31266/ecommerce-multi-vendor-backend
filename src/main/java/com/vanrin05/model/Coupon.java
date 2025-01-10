package com.vanrin05.model;

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

    double discountPercentage;

    LocalDateTime validityStartDate;

    LocalDateTime validityEndDate;

    double minimumOrderValue;

    boolean isActive;

    @ManyToMany(mappedBy = "usedCoupons")
    Set<User> usedByUsers;

    @PrePersist
    public void prePersist() {
        this.isActive = true;
        this.usedByUsers = new HashSet<>();
    }
}
