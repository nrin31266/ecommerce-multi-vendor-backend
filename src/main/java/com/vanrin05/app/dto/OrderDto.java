package com.vanrin05.app.dto;

import com.vanrin05.app.domain.PAYMENT_METHOD;
import com.vanrin05.app.model.Address;
import com.vanrin05.app.model.User;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDto {
    Long id;
    User user;
    Address shippingAddress;
    Long totalItemsPrice;
    Long originalPrice;
    Long finalPrice;
    int discountPercentage;
    int totalItem;
    PAYMENT_METHOD paymentMethod;
    LocalDateTime orderDate;
}
