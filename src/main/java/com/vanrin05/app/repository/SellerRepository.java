package com.vanrin05.app.repository;

import com.vanrin05.app.domain.ACCOUNT_STATUS;
import com.vanrin05.app.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    Optional<Seller> findByEmail(String email);
    List<Seller> findByAccountStatus(ACCOUNT_STATUS accountStatus);
    List<Seller> findAllByIdIn(Set<Long> ids);
}
