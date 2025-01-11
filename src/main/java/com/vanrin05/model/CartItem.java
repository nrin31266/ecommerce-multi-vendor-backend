package com.vanrin05.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity(name = "cart-items")
@Data
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
    Cart cart;

    @ManyToOne
    Product product;

    String size;

    int quantity;

    Integer mrpPrice;

    Integer sellingPrice;

    Long userId;

    @PrePersist
    protected void prePersist() {
        this.quantity = 0;
    }


}
