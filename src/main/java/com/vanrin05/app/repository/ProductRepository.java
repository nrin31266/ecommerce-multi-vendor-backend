package com.vanrin05.app.repository;


import com.vanrin05.app.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    List<Product> findBySellerId(Long sellerId);

    @Query("SELECT p FROM products p WHERE (:query is null or lower(p.title) LIKE lower(concat('%' , :query, '%')))" +
           "OR (:query is null or lower(p.category.name) LIKE lower(concat('%', :query, '%'))) ")
    List<Product> searchProduct(@Param("query")String query);
}
