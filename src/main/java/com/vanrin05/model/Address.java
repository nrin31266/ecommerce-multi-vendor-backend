package com.vanrin05.model;

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

    String locality;

    String address;

    String city;

    String state;

    String pinCode;

    String mobile;


    // :))
    String zipCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;
}
