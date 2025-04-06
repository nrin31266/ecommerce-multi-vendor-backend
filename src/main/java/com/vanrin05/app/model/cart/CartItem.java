package com.vanrin05.app.model.cart;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vanrin05.app.model.product.Product;
import com.vanrin05.app.model.product.SubProduct;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "cart_id")
    Cart cart;
    @ManyToOne

    @JoinColumn(name = "product_id")
    Product product;

    @ManyToOne
    @JoinColumn(name = "sub_product_id")
    SubProduct subProduct;


//    String size;
    int quantity;

//    Integer mrpPrice;
//    Integer sellingPrice;
    Long userId;
}
