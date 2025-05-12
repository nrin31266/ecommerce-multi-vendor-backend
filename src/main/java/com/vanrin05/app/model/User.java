package com.vanrin05.app.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vanrin05.app.domain.USER_ROLE;
import com.vanrin05.app.model.cart.Coupon;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "pickup_address_id")
    Address pickupAddress;




    @JsonManagedReference
    @JsonIgnore
    @OneToMany
    Set<Address> addresses = new HashSet<>();


    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinTable(
            name = "user_coupons",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "coupon_id")
    )
    Set<Coupon> usedCoupons = new HashSet<>();

    public User(String email,String fullName, String mobile, String password) {
        this.email = email;
        this.fullName = fullName;
        this.mobile = mobile;
        this.password = password;
    }

    @PrePersist
    protected void prePersist() {
        this.role = USER_ROLE.ROLE_CUSTOMER;
    }
}
