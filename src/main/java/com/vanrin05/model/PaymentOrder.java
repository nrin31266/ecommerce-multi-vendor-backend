package com.vanrin05.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vanrin05.domain.PAYMENT_METHOD;
import com.vanrin05.domain.PAYMENT_ORDER_STATUS;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
