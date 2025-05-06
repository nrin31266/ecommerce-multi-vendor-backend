package com.vanrin05.app.dto.request;

import com.vanrin05.app.domain.PAYMENT_METHOD;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateOrderRequest {
    PAYMENT_METHOD paymentMethod;
    Long addressId;
    String shippingCoupon;
    String shopCoupon;
    String platformCoupon;
}
