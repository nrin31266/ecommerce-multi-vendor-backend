package com.vanrin05.model;

import com.vanrin05.domain.PAYMENT_METHOD;
import com.vanrin05.domain.PAYMENT_ORDER_STATUS;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity(name = "payment-orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long amount;

    PAYMENT_ORDER_STATUS paymentOrderStatus;

    PAYMENT_METHOD paymentMethod;

    String paymentLinkId;

    @ManyToOne
    User user;

    @OneToMany
    Set<Order> orders;

    @PrePersist
    protected void onCreate() {
        this.orders = new HashSet<>();
        this.paymentOrderStatus = PAYMENT_ORDER_STATUS.PENDING;
    }
}
