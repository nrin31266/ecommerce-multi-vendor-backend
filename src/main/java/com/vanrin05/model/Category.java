package com.vanrin05.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category {

    public Category(String name, String categoryId, Integer level, String parentCategory) {
        this.name = name;
        this.categoryId = categoryId;
        this.parentCategory = parentCategory;
        this.level = level;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;



    @NotNull
    @Column(unique = true, nullable = false)
    String categoryId;


    String parentCategory;

    @NotNull
    Integer level;

}
