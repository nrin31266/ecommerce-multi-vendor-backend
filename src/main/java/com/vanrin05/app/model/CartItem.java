package com.vanrin05.app.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
    Product product;
    String size;
    int quantity;
    Integer mrpPrice;
    Integer sellingPrice;
    Long userId;
}
