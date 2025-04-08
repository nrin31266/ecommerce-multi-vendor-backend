package com.vanrin05.app.model.orderpayment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.vanrin05.app.domain.ORDER_ITEM_STATUS;
import com.vanrin05.app.model.product.Product;
import com.vanrin05.app.model.product.SubProduct;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    Order order;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    Product product;

    @ManyToOne@JoinColumn(name = "sub_product_id")
    SubProduct subProduct;

    int quantity;
    Integer mrpPrice;
    Integer sellingPrice;
    Long userId;


    String cancelReason;
    @Enumerated(EnumType.STRING)
    ORDER_ITEM_STATUS status;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "paymentStatus", column = @Column(name = "payment_status")),
    })
    PaymentDetails paymentDetails = new PaymentDetails();

    @CreatedDate
    LocalDateTime createdDate;

    @LastModifiedDate
    LocalDateTime updatedDate;


    LocalDateTime deliveryDate;

    @PrePersist
    protected void onCreate() {
        deliveryDate = LocalDateTime.now().plusDays(7);
    }

}
