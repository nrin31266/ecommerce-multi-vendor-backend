package com.vanrin05.app.model;

import com.vanrin05.app.model.product.Product;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    User user;

    @ManyToMany
    Set<Product> products;

    @PrePersist
    protected void onCreate() {
        this.products = new HashSet<>();
    }
}
