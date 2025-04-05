package com.vanrin05.app.service.impl;

import com.vanrin05.app.dto.request.CreateProductReq;
import com.vanrin05.app.dto.request.CreateSubProductReq;
import com.vanrin05.app.dto.request.UpdateProductReq;
import com.vanrin05.app.exception.AppException;
import com.vanrin05.app.exception.ErrorCode;
import com.vanrin05.app.mapper.ProductMapper;
import com.vanrin05.app.model.Category;
import com.vanrin05.app.model.Seller;
import com.vanrin05.app.model.product.Product;
import com.vanrin05.app.model.product.ProductOptionType;
import com.vanrin05.app.model.product.SubProduct;
import com.vanrin05.app.model.product.SubProductOption;
import com.vanrin05.app.repository.CategoryRepository;
import com.vanrin05.app.repository.ProductRepository;
import com.vanrin05.app.repository.SubProductRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
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

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ProductService {
    ProductRepository productRepository;
    ProductMapper productMapper;
    CategoryService categoryService;
    SellerService sellerService;
    SubProductRepository subProductRepository;
    CategoryRepository categoryRepository;

    private List<Category> findAllCategoryInIds(List<String> ids) {
        return categoryRepository
                .findAllByCategoryIdIn(ids);
    }

    @Transactional
    public Product createProduct(CreateProductReq req, String jwt) {
        List<Category> categories = findAllCategoryInIds(List.of(req.getCategory1(), req.getCategory2(), req.getCategory3()));
        if(categories.size() < 3){
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        Product product = productMapper.toProduct(req);
        product.setCategory(categories.get(2));

        product.setSeller(sellerService.getSellerProfile(jwt));
//        product= productRepository.save(product);
        if (req.getIsSubProduct() != null && req.getIsSubProduct()) {
            if(req.getMrpPrice() == null || req.getSellingPrice() == null || req.getQuantity() == null){
                throw new AppException("Please fill full field of product");
            }
            SubProduct subProduct = new SubProduct();
            subProduct.setQuantity(req.getQuantity());
            subProduct.setImages(req.getImages());
            subProduct.setSellingPrice(req.getSellingPrice());
            subProduct.setMrpPrice(req.getMrpPrice());
            subProduct.setDiscountPercentage(discountPercentage(req.getMrpPrice(), req.getSellingPrice()));
            subProduct.setOptions(null);
            subProduct.setProduct(product);
//            subProductRepository.save(subProduct);
            product.setSubProducts(List.of(subProduct));
            product.setDiscountPercentage(discountPercentage(req.getMrpPrice(), req.getSellingPrice()));
            product.setMinSellingPrice(req.getSellingPrice());
            product.setMaxSellingPrice(req.getSellingPrice());
            product.setMaxMrpPrice(req.getMrpPrice());
            product.setMinMrpPrice(req.getMrpPrice());
            product.setTotalSubProduct(1);
        } else {
            if (req.getOptionsTypes().isEmpty()) {
                throw new AppException("Options types are empty");
            }
            if (req.getOptionKey() != null && !req.getOptionsTypes().contains(req.getOptionKey())) {
                throw new AppException("Option key invalid");
            }
            Set<ProductOptionType> productOptionTypes = new HashSet<>();
            for(String optionType : req.getOptionsTypes()) {
                productOptionTypes.add(
                        ProductOptionType.builder()
                                .value(optionType)
                                .build()
                );
            }
            product.setOptionsTypes(productOptionTypes);
        }
        productRepository.save(product);
        return product;
    }

    @Transactional
    public SubProduct addSubProductToProduct(Long productId, CreateSubProductReq req, String jwt) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new AppException("Product not found"));
        if (product.getIsSubProduct() != null && product.getIsSubProduct()) {
            throw new AppException("This product is a sub product");
        }
        Seller seller = sellerService.getSellerProfile(jwt);
        if (!seller.getId().equals(product.getSeller().getId())) {
            throw new AppException("Seller's id not match");
        }

        SubProduct subProduct = new SubProduct();
        subProduct.setQuantity(req.getQuantity());
        subProduct.setImages(req.getImages());
        subProduct.setSellingPrice(req.getSellingPrice());
        subProduct.setMrpPrice(req.getMrpPrice());
//        subProduct.setOptions(null);

        Set<SubProductOption> subProductOptions = new HashSet<>();
        for (ProductOptionType productOptionType : product.getOptionsTypes()){
            SubProductOption subProductOption = SubProductOption.builder()
                    .optionValue(req.getOptions().get(productOptionType.getValue()))
                    .optionType(productOptionType)
                    .subProduct(subProduct)
                    .build();
            subProductOptions.add(subProductOption);
        }
        subProduct.setOptions(subProductOptions);
        subProduct.setDiscountPercentage(discountPercentage(req.getMrpPrice(), req.getSellingPrice()));
        subProduct.setProduct(product);

        product.getSubProducts().add(subProduct);
        // MRPs
        if (product.getMinMrpPrice() == null || req.getMrpPrice() < product.getMinMrpPrice()) {
            product.setMinMrpPrice(req.getMrpPrice());
        }
        if (product.getMaxMrpPrice() == null || req.getMrpPrice() > product.getMaxMrpPrice()) {
            product.setMaxMrpPrice(req.getMrpPrice());
        }

        // Selling Prices
        if (product.getMinSellingPrice() == null || req.getSellingPrice() < product.getMinSellingPrice()) {
            product.setMinSellingPrice(req.getSellingPrice());
        }
        if (product.getMaxSellingPrice() == null || req.getSellingPrice() > product.getMaxSellingPrice()) {
            product.setMaxSellingPrice(req.getSellingPrice());
        }




        product.setTotalSubProduct(product.getTotalSubProduct() + 1);
        product.setDiscountPercentage(discountPercentage(product.getMaxMrpPrice(), product.getMinSellingPrice()));
        productRepository.save(product);

        return subProductRepository.save(subProduct);
    }


    private int discountPercentage(double mrpPrice, double sellingPrice) {

        if (mrpPrice < sellingPrice || mrpPrice <= 0) {
            throw new AppException("Mrp price is invalid. Mrp: " + mrpPrice + ", Selling price: " + sellingPrice);
        }
        double discount = (mrpPrice - sellingPrice);
        double percentage = discount / mrpPrice * 100;
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
        List<Category> categories = findAllCategoryInIds(List.of(req.getCategory1(), req.getCategory2(), req.getCategory3()));
        if(categories.size() < 3){
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        Product product = this.findProductById(productId);
        product.setCategory(categories.get(2));
        productMapper.updateProduct(product, req);
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
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("totalSubProduct"), 1));

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
