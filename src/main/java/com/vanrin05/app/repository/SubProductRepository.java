package com.vanrin05.app.repository;

import com.vanrin05.app.model.product.SubProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubProductRepository extends JpaRepository<SubProduct, Long> {
}
