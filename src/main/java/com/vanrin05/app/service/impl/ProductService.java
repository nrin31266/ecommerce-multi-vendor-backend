package com.vanrin05.app.service.impl;

import com.vanrin05.app.dto.request.CreateProductReq;
import com.vanrin05.app.dto.request.UpdateProductReq;
import com.vanrin05.app.exception.AppException;
import com.vanrin05.app.mapper.ProductMapper;
import com.vanrin05.app.model.Category;
import com.vanrin05.app.model.Product;
import com.vanrin05.app.repository.CategoryRepository;
import com.vanrin05.app.repository.ProductRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ProductService {
    ProductRepository productRepository;
    ProductMapper productMapper;
    CategoryRepository categoryRepository;
    CategoryService categoryService;
    SellerService sellerService;

    public Product createProduct(CreateProductReq req, String jwt) {


        Category category1 = categoryService.findOrCreateCategory(req.getCategory1(), null, 1);
        Category category2 = categoryService.findOrCreateCategory(req.getCategory2(), category1, 2);
        Category category3 = categoryService.findOrCreateCategory(req.getCategory3(), category2, 3);

        Product product = productMapper.toProduct(req);
        product.setCategory(category3);
        product.setDiscountPercentage(discountPercentage(product.getMrpPrice(), product.getSellingPrice()));
        product.setSeller(sellerService.getSellerProfile(jwt));


        return productRepository.save(product);
    }


    private int discountPercentage(double mrpPrice, double sellingPrice) {

        if (mrpPrice < sellingPrice || mrpPrice <= 0) {
            throw new AppException("Mrp price is invalid. Mrp: " + mrpPrice + ", Selling price: " + sellingPrice);
        }
        double discount = (mrpPrice - sellingPrice);
        double percentage = discount / sellingPrice * 100;
        return (int) percentage;
    }

    public void deleteProduct(Long productId) {
        Product product = this.findProductById(productId);
        productRepository.delete(product);
    }

    public Product findProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new AppException("Product not found with id: " + productId));
    }

    public Product updateProduct(Long productId, UpdateProductReq req) {
        Category category1 = categoryService.findOrCreateCategory(req.getCategory1(), null, 1);
        Category category2 = categoryService.findOrCreateCategory(req.getCategory2(), category1, 2);
        Category category3 = categoryService.findOrCreateCategory(req.getCategory3(), category2, 3);
        Product product = this.findProductById(productId);
        product.setCategory(category3);
        productMapper.updateProduct(product, req);
        product.setDiscountPercentage(discountPercentage(product.getMrpPrice(), product.getSellingPrice()));
        return productRepository.save(product);
    }

    public List<Product> searchProduct(String query) {
        return productRepository.searchProduct(query);
    }

    public Page<Product> getAllProduct(
            String category, String brand,
            String colors, String sizes,
            Integer minimumPrice, Integer maximumPrice,
            Integer minimumDiscount,
            String sort,
            String stock,
            Integer pageNumber
    ) {
        Specification<Product> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (category != null) {
                Join<Product, Category> joinCategory = root.join("category");
                predicates.add(criteriaBuilder.equal(joinCategory.get("categoryId"), category));
            }
            if (brand != null && !brand.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("brand"), brand));
            }

            if (stock != null && !stock.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("stock"), stock));
            }

            if (colors != null && !colors.isEmpty()) {
                List<String> colorList = Arrays.asList(colors.split(","));
                predicates.add(root.get("color").in(colorList));
            }

            if (sizes != null && !sizes.isEmpty()) {
                List<String> sizeList = Arrays.asList(sizes.split(","));
                predicates.add(root.get("size").in(sizeList));
            }

            if (minimumPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("sellingPrice"), minimumPrice));
            }

            if (maximumPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("sellingPrice"), maximumPrice));
            }

            if (minimumDiscount != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("discountPercentage"), minimumDiscount));
            }


            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Pageable pageable;
        Map<String, Sort> sortMap = Map.of(
                "price_low", Sort.by("sellingPrice").ascending(),
                "price_high", Sort.by("sellingPrice").descending(),
                "discount_high", Sort.by("discountPercentage").descending()
        );
        Sort sortOption = sort != null && sortMap.containsKey(sort) ? sortMap.get(sort) : Sort.unsorted();
        pageable = PageRequest.of(pageNumber != null ? (pageNumber - 1) : 0, 10, sortOption);

        return productRepository.findAll(specification, pageable);
    }

    public List<Product> getProductsBySellerId(Long sellerId) {
        return productRepository.findBySellerId(sellerId);
    }

}
