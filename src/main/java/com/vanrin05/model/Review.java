package com.vanrin05.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    List<String> productImages;

    @JsonIgnore
    @ManyToOne
    Product product;

    @ManyToOne
    User user;

    @Column(nullable = false)
    LocalDateTime createdAt;

    @PrePersist
    protected void prePersist() {
        this.productImages = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
    }
}
