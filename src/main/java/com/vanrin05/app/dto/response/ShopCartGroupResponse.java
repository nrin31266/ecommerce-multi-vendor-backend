package com.vanrin05.app.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vanrin05.app.dto.CartDto;
import com.vanrin05.app.model.Seller;
import com.vanrin05.app.model.cart.CartItem;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShopCartGroupResponse {
    Seller seller;
    List<CartItem> cartItems;
}
