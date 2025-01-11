package com.vanrin05.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vanrin05.domain.USER_ROLE;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String password;

    String email;

    String fullName;

    String mobile;

    @Enumerated(EnumType.STRING)
    USER_ROLE role;

    @OneToMany
    Set<Address> addresses;

    @ManyToMany
    @JsonIgnore
    Set<Coupon> usedCoupons;

    @PrePersist
    protected void prePersist() {
        this.role = USER_ROLE.ROLE_CUSTOMER;
        this.addresses = new HashSet<>();
        this.usedCoupons = new HashSet<>();
    }
}
