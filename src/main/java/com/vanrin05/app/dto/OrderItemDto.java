package com.vanrin05.app.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.stripe.model.PaymentMethod;
import com.vanrin05.app.domain.ORDER_ITEM_STATUS;
import com.vanrin05.app.model.Address;
import com.vanrin05.app.model.orderpayment.Order;
import com.vanrin05.app.model.orderpayment.PaymentDetails;
import com.vanrin05.app.model.product.Product;
import com.vanrin05.app.model.product.SubProduct;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItemDto {
    Long id;
    ProductDto product;
    SubProduct subProduct;
    int quantity;
    Long mrpPrice;
    Long sellingPrice;
    Long userId;
    String cancelReason;
    ORDER_ITEM_STATUS status;
    PaymentDetails paymentDetails;
    LocalDateTime createdDate;
    LocalDateTime updatedDate;
    LocalDateTime deliveryDate;
    PaymentMethod paymentMethod;
    Address shippingAddress;
    Long orderId;

}
