package com.vanrin05.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.vanrin05.app.model.product.Product;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WishlistItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;


    @JoinColumn(nullable = false, name = "user_id")
    @ManyToOne
    User user;

    @ManyToOne
    Product product;

    LocalDateTime addedAt;

    @PrePersist
    protected void onCreate() {
        addedAt = LocalDateTime.now();
    }
}
