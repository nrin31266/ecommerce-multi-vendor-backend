package com.vanrin05.app.model.product;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    int quantity;
    Long mrpPrice;
    Long sellingPrice;
    int discountPercentage;
    @ElementCollection
    List<String> images;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToMany(mappedBy = "subProduct")
    private Set<SubProductOption> options;


}
