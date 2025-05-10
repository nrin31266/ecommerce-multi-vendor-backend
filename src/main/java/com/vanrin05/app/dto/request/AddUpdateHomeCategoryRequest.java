package com.vanrin05.app.dto.request;

import com.vanrin05.app.domain.HOME_CATEGORY_SECTION;
import com.vanrin05.app.model.HomeCategory;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

//HOME_FURNITURE
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddUpdateHomeCategoryRequest {
    String name;
    String image;
    List<String> categoryIds;
    HOME_CATEGORY_SECTION homeCategorySection;
}
