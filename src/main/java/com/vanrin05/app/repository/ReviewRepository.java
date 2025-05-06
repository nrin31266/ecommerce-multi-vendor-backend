package com.vanrin05.app.repository;

import com.vanrin05.app.model.Review;
import com.vanrin05.app.model.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProduct(Product product);
    Optional<Review> findFirstByProduct(Product product);
}
