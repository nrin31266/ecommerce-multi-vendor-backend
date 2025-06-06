package com.vanrin05.app.dto;

import com.vanrin05.app.dto.response.ShopCartGroupResponse;
import com.vanrin05.app.model.User;
import com.vanrin05.app.model.cart.CartItem;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartDto {
    Long id;
    User user;
//    Long totalSellingPrice = 0L;
//    int totalItems = 0;
//    Long totalMrpPrice = 0L;
//    int discount;
    String couponCode;
    List<ShopCartGroupResponse> groups;
}
