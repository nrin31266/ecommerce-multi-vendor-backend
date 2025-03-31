package com.vanrin05.app.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateProductReq {
    String title;
    String description;
    int mrpPrice;
    int sellingPrice;
    int quantity;
    String color;
    List<String> images;
    String category1;
    String category2;
    String category3;
    String sizes;
}
