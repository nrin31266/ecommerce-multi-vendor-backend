package com.vanrin05.app.repository;


import com.vanrin05.app.model.Category;
import com.vanrin05.app.model.Seller;
import com.vanrin05.app.model.product.Product;
import org.springframework.data.domain.Pageable;
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

    @Query("""
                SELECT p FROM products p 
                WHERE p.category = :category AND p.id <> :excludedId
            """)
    List<Product> findByCategoryExcludingProduct(@Param("category") Category category,
                                                 @Param("excludedId") Long excludedId,
                                                 Pageable pageable);




//    @Modifying
//    @Query("UPDATE products p SET p.quantity = p.quantity + :quantity WHERE p.id = :id")
//    void incrementProductQuantity(@Param("id") Long id, @Param("quantity") int quantity);


}
