package com.vanrin05.app.dto.response;

import com.vanrin05.app.model.Category;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoriesResponse {
    List<Category> one;
    List<Category> two;
    List<Category> three;
    Map<String, List<Category>> m2;
    Map<String, List<Category>> m3;
    Map<String, List<Category>> m3bym1;
}
