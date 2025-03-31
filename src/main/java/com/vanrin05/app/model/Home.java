package com.vanrin05.app.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Home {

    List<HomeCategory> grids;

    List<HomeCategory> shopByCategories;

    List<HomeCategory> electricCategories;

    List<HomeCategory> dealerCategories;

    List<Deal> deals;
}
