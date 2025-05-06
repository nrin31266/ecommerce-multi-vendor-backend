package com.vanrin05.app.model.orderpayment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.vanrin05.app.domain.ORDER_ITEM_STATUS;
import com.vanrin05.app.model.Seller;
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
    @JoinColumn(name = "seller_order_id", nullable = false)
    SellerOrder sellerOrder;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    Product product;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sub_product_id")
    SubProduct subProduct;
    @Column(nullable = false)
    int quantity;
    Long mrpPrice;
    @Column(nullable = false)
    Long sellingPrice;
    @Column(nullable = false)
    Long userId;
}
