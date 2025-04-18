package com.vanrin05.app.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateProductReq {
    String title;
    String description;
    List<String> images;
    String category1;
    String category2;
    String category3;
    Set<String> optionsTypes;
    String optionKey;
    Boolean isSubProduct;
    Integer quantity;
    Long mrpPrice;
    Long sellingPrice;

}
