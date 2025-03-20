package com.vanrin05.dto.request;

import com.vanrin05.model.Category;
import com.vanrin05.model.Seller;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
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
