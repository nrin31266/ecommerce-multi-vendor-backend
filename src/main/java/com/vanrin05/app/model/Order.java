package com.vanrin05.app.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vanrin05.app.domain.ORDER_STATUS;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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

    Long sellerId;


    @JsonManagedReference
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderItem> orderItems;

    @ManyToOne
    @JoinColumn(name = "shipping_address_id")
    Address shippingAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "paymentStatus", column = @Column(name = "payment_status"))
    })
    PaymentDetails paymentDetails = new PaymentDetails();

    double totalMrpPrice;

    Integer totalSellingPrice;

    Integer discount;

    Integer totalItem;

    @Enumerated(EnumType.STRING)
    ORDER_STATUS orderStatus;



    @JsonBackReference
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
