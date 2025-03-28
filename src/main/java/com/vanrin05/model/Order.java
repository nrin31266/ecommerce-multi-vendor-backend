package com.vanrin05.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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


    @ManyToOne
    User user;

    Long sellerId;

    @JsonIgnore
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderItem> orderItems = new ArrayList<>();

    @ManyToOne
    Address shippingAddress;

    @Embedded
    PaymentDetails paymentDetails = new PaymentDetails();

    double totalMrpPrice;

    Integer totalSellingPrice;

    Integer discount;

    Integer totalItem;

    @Enumerated(EnumType.STRING)
    ORDER_STATUS orderStatus;

    @ManyToOne
    @JoinColumn(name = "payment_order_id")
    PaymentOrder paymentOrder;

    LocalDateTime orderDate;
    LocalDateTime deliveryDate;

    @PrePersist
    protected void onCreate() {
        this.orderDate = LocalDateTime.now();
        this.deliveryDate = this.orderDate.plusDays(7);
    }
}
