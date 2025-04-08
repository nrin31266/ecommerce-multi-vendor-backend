package com.vanrin05.app.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.vanrin05.app.model.product.Product;
import com.vanrin05.app.model.product.SubProductOption;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubProductDto {
    Long id;
    int quantity;
    Long mrpPrice;
    Long sellingPrice;
    int discountPercentage;
    List<String> images;
    private Set<SubProductOption> options;
    Long productId;
}
