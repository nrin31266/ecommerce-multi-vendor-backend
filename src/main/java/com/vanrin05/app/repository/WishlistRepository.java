package com.vanrin05.app.repository;


import com.vanrin05.app.model.User;
import com.vanrin05.app.model.WishlistItem;
import com.vanrin05.app.model.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<WishlistItem, Long> {
    List<WishlistItem> findByUser(User user);
    Optional<WishlistItem> findByUserAndProduct(User user, Product product);
}
