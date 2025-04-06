package com.vanrin05.app.repository;

import com.vanrin05.app.model.cart.Cart;
import com.vanrin05.app.model.cart.CartItem;
import com.vanrin05.app.model.product.Product;
import com.vanrin05.app.model.product.SubProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProductAndSubProduct(Cart cart, Product product, SubProduct subProduct);
    CartItem findCartItemById(Long id);
}
