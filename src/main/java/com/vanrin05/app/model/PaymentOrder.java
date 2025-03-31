package com.vanrin05.app.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vanrin05.app.domain.PAYMENT_METHOD;
import com.vanrin05.app.domain.PAYMENT_ORDER_STATUS;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "payment_orders")
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
    @Enumerated(EnumType.STRING)
    PAYMENT_ORDER_STATUS paymentOrderStatus;
    @Enumerated(EnumType.STRING)
    PAYMENT_METHOD paymentMethod;



    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;


    @JsonManagedReference
    @OneToMany(mappedBy = "paymentOrder", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Order> orders;


    @PrePersist
    protected void onCreate() {
        this.orders = new ArrayList<>();
        this.paymentOrderStatus = PAYMENT_ORDER_STATUS.PENDING;
    }
}
