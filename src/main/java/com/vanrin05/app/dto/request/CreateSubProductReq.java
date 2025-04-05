package com.vanrin05.app.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateSubProductReq {
    List<String> images;
    int quantity;
    Long mrpPrice;
    Long sellingPrice;
    Map<String, String> options;
}
