package com.vanrin05.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String title;
    String description;
    int mrpPrice;
    int sellingPrice;
    int discountPercentage;
    int quantity;
    String color;

    @ElementCollection
    List<String> images = new ArrayList<>();

    int numberRating;

    @ManyToOne
    Category category;

    @ManyToOne
    Seller seller;



    LocalDateTime createdAt;

    String sizes;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Review> reviews;

    @PrePersist
    protected void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.reviews = new ArrayList<>();
    }
}
