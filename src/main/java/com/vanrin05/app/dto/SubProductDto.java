package com.vanrin05.app.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SubProductDto {
    private Long id;
    private int quantity;
    private Long mrpPrice;
    private Long sellingPrice;
    private int discountPercentage;
    private List<String> images;

    private Map<String, String> options;
}
