package com.vanrin05.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vanrin05.app.model.product.Product;
import com.vanrin05.app.model.product.SubProduct;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false, length = 999)
    String reviewText;

    @ElementCollection
    List<String> reviewImages = new ArrayList<>();

    Integer reviewRating;

    @JsonIgnore
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "product_id")
    Product product;

    @ManyToOne
    SubProduct subProduct;

    @ManyToOne
    User user;

    @Column(nullable = false)
    LocalDateTime createdAt;

    @PrePersist
    protected void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
