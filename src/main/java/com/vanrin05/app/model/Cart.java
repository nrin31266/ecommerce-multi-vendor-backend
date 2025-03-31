package com.vanrin05.app.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @OneToOne
    User user;
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<CartItem> cartItems;
    double totalSellingPrice;
    int totalItems;
    int totalMrpPrice;
    int discount;
    String couponCode;

    public Cart(User user) {
        this.user = user;
    }

    @PrePersist
    protected void prePersist() {
        this.cartItems = new HashSet<>();
        totalSellingPrice = 0;
        totalItems = 0;
        totalMrpPrice = 0;
        discount = 0;
    }
}
