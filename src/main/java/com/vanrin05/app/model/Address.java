package com.vanrin05.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    String phoneNumber;

    String street;

    String ward;

    String district;

    String province;

    String postalCode;    // Mã bưu điện

    @JsonIgnore
    @ManyToOne@JoinColumn(name = "user_id")
    User user;

    @JsonIgnore
    @ManyToOne@JoinColumn(name = "seller_id")
    Seller seller;
}

