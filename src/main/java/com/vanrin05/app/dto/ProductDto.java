package com.vanrin05.app.dto;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vanrin05.app.model.Category;
import com.vanrin05.app.model.Review;
import com.vanrin05.app.model.Seller;
import com.vanrin05.app.model.product.ProductOptionType;
import com.vanrin05.app.model.product.SubProduct;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDto {

    Long id;
    String title;
    String description;
    Long minMrpPrice;
    Long maxMrpPrice;
    int discountPercentage;
    Long minSellingPrice;
    Long maxSellingPrice;
    int totalSubProduct;
    Long totalSold;
    Boolean isSubProduct = false;
    List<String> images;
    int numberRating;
    Category category;
    Seller seller;
    LocalDateTime createdAt;

//    List<Review> reviews;


    Set<ProductOptionType> optionsTypes = new HashSet<>();
    String optionKey;

//    private List<SubProduct> subProducts = new ArrayList<>();

}
