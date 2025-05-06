package com.vanrin05.app.dto.response;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vanrin05.app.domain.PAYMENT_METHOD;
import com.vanrin05.app.domain.SELLER_ORDER_STATUS;
import com.vanrin05.app.model.Address;
import com.vanrin05.app.model.Seller;
import com.vanrin05.app.model.User;
import com.vanrin05.app.model.orderpayment.Order;
import com.vanrin05.app.model.orderpayment.OrderItem;
import com.vanrin05.app.model.orderpayment.PaymentDetails;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserOrderHistoryResponse {
    Long id;
//    Order order;
    Integer totalItem = 0;
    Long totalPrice = 0L;
    Long discountShipping = 0L;
    Long discountShop = 0L;
    Long discountPlatform = 0L;
    Long finalPrice = 0L;
    List<OrderItem> orderItems = new ArrayList<>();
    String cancelReason;
    SELLER_ORDER_STATUS status;
    PaymentDetails paymentDetails = new PaymentDetails();
    LocalDateTime createdDate;
    LocalDateTime updatedDate;
//    Seller seller;
    LocalDateTime deliveryDate;
    Boolean isApproved = false;
    Long userId;

    //Dto
    User customer;
    Address shippingAddress;
    PAYMENT_METHOD paymentMethod;
}
