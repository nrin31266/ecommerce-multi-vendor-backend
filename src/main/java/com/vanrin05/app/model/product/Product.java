package com.vanrin05.app.model.product;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vanrin05.app.model.Category;
import com.vanrin05.app.model.Review;
import com.vanrin05.app.model.Seller;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "products")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String title;
    @Column(length = 9999)
    String description;
    Long minMrpPrice;
    Long maxMrpPrice;
    int discountPercentage;
    Long minSellingPrice;
    Long maxSellingPrice;
    int totalSubProduct;
    int totalSold;
    int totalOrder;
    Boolean isSubProduct = false;
    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    List<String> images;
    int numberRating;
    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
    @ManyToOne
    Seller seller;
    LocalDateTime createdAt;
    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Review> reviews;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    Set<ProductOptionType> optionsTypes = new HashSet<>();
    String optionKey;
    @JsonManagedReference
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubProduct> subProducts = new ArrayList<>();
    @PrePersist
    protected void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.reviews = new ArrayList<>();
    }
    @PreRemove
    private void preRemove() {
        images.clear();
    }
}
