package com.vanrin05.app.dto;

import com.vanrin05.app.model.product.SubProduct;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItemDto {

    Long id;
//    Cart cart;
    ProductDto product;
    SubProduct subProduct;

    String size;
    int quantity;
    Integer mrpPrice;
    Integer sellingPrice;
    Long userId;
}

