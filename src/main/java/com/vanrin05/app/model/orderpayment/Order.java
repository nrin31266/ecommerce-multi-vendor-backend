package com.vanrin05.app.model.orderpayment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vanrin05.app.domain.PAYMENT_METHOD;
import com.vanrin05.app.model.Address;
import com.vanrin05.app.model.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
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
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @JsonManagedReference
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderItem> orderItems;

    @ManyToOne
    @JoinColumn(name = "shipping_address_id")
    Address shippingAddress;

    Long totalMrpPrice;
    Long totalSellingPrice;
    int discountPercentage;
    int totalItem;
    int totalQuantity;
    @Enumerated(EnumType.STRING)

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "payment_order_id")
    Payment payment;

    PAYMENT_METHOD paymentMethod;

    @CreatedDate
    LocalDateTime orderDate;


}
