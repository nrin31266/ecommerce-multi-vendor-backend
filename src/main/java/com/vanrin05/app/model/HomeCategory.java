package com.vanrin05.app.model;

import com.vanrin05.app.domain.HOME_CATEGORY_SECTION;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class HomeCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String image;
    String categoryIds;
    @Enumerated(EnumType.STRING)
    HOME_CATEGORY_SECTION homeCategorySection;
}
