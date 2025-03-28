package com.vanrin05.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @Enumerated(EnumType.STRING)
    PAYMENT_ORDER_STATUS paymentOrderStatus;
    @Enumerated(EnumType.STRING)
    PAYMENT_METHOD paymentMethod;



    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;


    @OneToMany(mappedBy = "paymentOrder", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    Set<Order> orders = new HashSet<>();


    @PrePersist
    protected void onCreate() {
        this.orders = new HashSet<>();
        this.paymentOrderStatus = PAYMENT_ORDER_STATUS.PENDING;
    }
}
