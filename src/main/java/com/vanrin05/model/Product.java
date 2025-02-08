package com.vanrin05.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Cascade;

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
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    List<String> images = new ArrayList<>();

    int numberRating;

    @ManyToOne(cascade = CascadeType.ALL)
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
