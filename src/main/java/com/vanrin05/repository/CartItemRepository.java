package com.vanrin05.repository;

import com.vanrin05.model.Cart;
import com.vanrin05.model.CartItem;
import com.vanrin05.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProductAndSize(Cart cart, Product product, String size);
    CartItem findCartItemById(Long id);
}
