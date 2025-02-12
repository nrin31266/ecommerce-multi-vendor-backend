package com.vanrin05.repository;


import com.vanrin05.model.User;
import com.vanrin05.model.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    Wishlist findByUserId(Long userId);
}
