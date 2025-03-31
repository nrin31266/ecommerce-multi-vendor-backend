package com.vanrin05.app.repository;

import com.vanrin05.app.model.Cart;
import com.vanrin05.app.model.CartItem;
import com.vanrin05.app.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProductAndSize(Cart cart, Product product, String size);
    CartItem findCartItemById(Long id);
}
