package com.vanrin05.app.model.orderpayment;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vanrin05.app.domain.PAYMENT_METHOD;
import com.vanrin05.app.domain.PAYMENT_ORDER_STATUS;
import com.vanrin05.app.model.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long amount;
    @Enumerated(EnumType.STRING)
    PAYMENT_ORDER_STATUS paymentOrderStatus;
    @Enumerated(EnumType.STRING)
    PAYMENT_METHOD paymentMethod;

    LocalDateTime expiryDate;


    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;


    @JsonManagedReference
    @OneToOne
    Order order;



}
