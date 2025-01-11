package com.vanrin05.model;

import com.vanrin05.domain.ORDER_STATUS;
import com.vanrin05.domain.PAYMENT_STATUS;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String orderId;

    @ManyToOne
    User user;

    Long sellerId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderItem> orderItems;

    @ManyToOne
    Address shippingAddress;

    @Embedded
    PaymentDetails paymentDetails;

    double totalMrpPrice;

    Integer totalSellingPrice;

    Integer discount;

    @Enumerated(EnumType.STRING)
    ORDER_STATUS orderStatus;

    PAYMENT_STATUS paymentStatus;

    LocalDateTime orderDate;
    LocalDateTime deliveryDate;

    @PrePersist
    protected void onCreate() {
        this.orderItems = new ArrayList<>();
        this.paymentDetails = new PaymentDetails();
        this.paymentStatus = PAYMENT_STATUS.PENDING;
        this.orderDate = LocalDateTime.now();
        this.deliveryDate = this.orderDate.plusDays(7);
    }
}