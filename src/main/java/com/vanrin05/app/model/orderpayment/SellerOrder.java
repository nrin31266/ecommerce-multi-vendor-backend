package com.vanrin05.app.model.orderpayment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vanrin05.app.domain.ORDER_ITEM_STATUS;
import com.vanrin05.app.domain.SELLER_ORDER_STATUS;
import com.vanrin05.app.model.Seller;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellerOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    Order order;

    Integer totalItem = 0;

    Long totalPrice = 0L;
    Long discountShipping = 0L;
    Long discountShop = 0L;

    Long finalPrice = 0L;

    Long shippingCost = 0L;

    @JsonManagedReference
    @OneToMany(mappedBy = "sellerOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    List<OrderItem> orderItems = new ArrayList<>();

    String cancelReason;
    @Enumerated(EnumType.STRING)
    SELLER_ORDER_STATUS status;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "paymentStatus", column = @Column(name = "payment_status")),
    })
    PaymentDetails paymentDetails = new PaymentDetails();


    LocalDateTime createdDate;


    LocalDateTime updatedDate;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    Seller seller;

    LocalDateTime deliveryDate;

    LocalDateTime deliveredDate;

    Boolean isApproved = false;

    @Column(nullable = false)
    Long userId;

    @PrePersist
    protected void onCreate() {
        deliveryDate = LocalDateTime.now().plusDays(7);
        isApproved = false;
        createdDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDate = LocalDateTime.now();
    }
}
