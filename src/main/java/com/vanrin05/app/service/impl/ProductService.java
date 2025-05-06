package com.vanrin05.app.service.impl;

import com.vanrin05.app.dto.PageableDto;
import com.vanrin05.app.dto.ProductDto;

import com.vanrin05.app.dto.SubProductDto;
import com.vanrin05.app.dto.request.CreateProductReq;
import com.vanrin05.app.dto.request.CreateSubProductReq;
import com.vanrin05.app.dto.request.UpdateProductReq;
import com.vanrin05.app.dto.request.UpdateSubProductReq;
import com.vanrin05.app.exception.AppException;
import com.vanrin05.app.exception.ErrorCode;
import com.vanrin05.app.mapper.PageableMapper;
import com.vanrin05.app.mapper.ProductMapper;
import com.vanrin05.app.mapper.SubProductMapper;
import com.vanrin05.app.model.Category;
import com.vanrin05.app.model.Seller;
import com.vanrin05.app.model.User;
import com.vanrin05.app.model.orderpayment.Order;
import com.vanrin05.app.model.orderpayment.OrderItem;
import com.vanrin05.app.model.orderpayment.SellerOrder;
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
    SubProductMapper subProductMapper;
    PageableMapper pageableMapper;


    public void deliveredOrder(SellerOrder sellerOrder){
        List<OrderItem> orderItems = sellerOrder.getOrderItems();
        List<Product> products = new ArrayList<>();
        for( OrderItem orderItem : orderItems ){
            Product product = orderItem.getProduct();
            product.setTotalSold(product.getTotalSold() + orderItem.getQuantity());
            products.add(product);
        }
        productRepository.saveAll(products);
    }

    private List<Category> findAllCategoryInIds(List<String> ids) {
        return categoryRepository
                .findAllByCategoryIdIn(ids);
    }

    @Transactional
    public Product createProduct(CreateProductReq req, String jwt) {
        List<Category> categories = findAllCategoryInIds(List.of(req.getCategory1(), req.getCategory2(), req.getCategory3()));
        if (categories.size() < 3) {
            throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        Product product = productMapper.toProduct(req);
        product.setCategory(categories.get(2));

        product.setSeller(sellerService.getSellerProfile(jwt));
//        product= productRepository.save(product);
        if (req.getIsSubProduct() != null && req.getIsSubProduct()) {
            if (req.getMrpPrice() == null || req.getSellingPrice() == null || req.getQuantity() == null) {
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
            if (req.getOptionKey() != null && !req.getOptionKey().isEmpty() && !req.getOptionsTypes().contains(req.getOptionKey())) {
                throw new AppException("Option key invalid");
            }
            Set<ProductOptionType> productOptionTypes = new HashSet<>();
            for (String optionType : req.getOptionsTypes()) {
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
        Product product = findProductById(productId);
        if (product.getIsSubProduct() != null && product.getIsSubProduct()) {
            throw new AppException("This product is a sub product");
        }
        Seller seller = sellerService.getSellerProfile(jwt);
        if (!seller.getId().equals(product.getSeller().getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        SubProduct subProduct = new SubProduct();
        subProduct.setQuantity(req.getQuantity());
        subProduct.setImages(req.getImages());
        subProduct.setSellingPrice(req.getSellingPrice());
        subProduct.setMrpPrice(req.getMrpPrice());
//        subProduct.setOptions(null);

        Set<SubProductOption> subProductOptions = new HashSet<>();
        for (ProductOptionType productOptionType : product.getOptionsTypes()) {
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
        subProduct.setProduct(product);

        return subProductRepository.save(subProduct);
    }

    @Transactional
    public SubProduct updateSubProduct(Long subProductId, UpdateSubProductReq req, String jwt) {
        SubProduct subProduct = subProductRepository.findById(subProductId).orElseThrow(
                () -> new AppException("Sub product not found")
        );
        Seller seller = sellerService.getSellerProfile(jwt);
        if (!seller.getId().equals(subProduct.getProduct().getSeller().getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        subProductMapper.updateSubProduct(subProduct, req);
        if (req.getOptions() != null) {
            subProduct.getOptions().forEach(option -> {
                String newValue = req.getOptions().get(option.getOptionType().getValue());
                if (newValue != null) {
                    option.setOptionValue(newValue);
                }
            });
        }

        SubProduct savedSubProduct = subProductRepository.save(subProduct);
        Product product = savedSubProduct.getProduct();
        updateProductMinMaxPrices(product);

        return savedSubProduct;
    }


    @Transactional
    public void deleteSubProduct(Long productId, String jwt, Long subProductId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new AppException("Product not found"));
        if (product.getIsSubProduct() != null && product.getIsSubProduct()) {
            throw new AppException("This product is a sub product");
        }
        Seller seller = sellerService.getSellerProfile(jwt);
        if (!seller.getId().equals(product.getSeller().getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        SubProduct subProduct = subProductRepository.findById(subProductId).orElseThrow(() -> new AppException("SubProduct not found"));
        if (!product.getSubProducts().contains(subProduct)) {
            throw new AppException("SubProduct does not belong to this Product");
        }
        product.setTotalSubProduct(product.getTotalSubProduct() - 1);
        product.getSubProducts().remove(subProduct);
        updateProductMinMaxPrices(product);


//        subProductRepository.delete(subProduct);

    }

    private void updateProductMinMaxPrices(Product product) {

        Long newMinMrpPrice = null;
        Long newMaxMrpPrice = null;
        Long newMinSellingPrice = null;
        Long newMaxSellingPrice = null;

        for (SubProduct subProduct : product.getSubProducts()) {
            if (newMinMrpPrice == null || subProduct.getMrpPrice() < newMinMrpPrice) {
                newMinMrpPrice = subProduct.getMrpPrice();
            }
            if (newMaxMrpPrice == null || subProduct.getMrpPrice() > newMaxMrpPrice) {
                newMaxMrpPrice = subProduct.getMrpPrice();
            }

            if (newMinSellingPrice == null || subProduct.getSellingPrice() < newMinSellingPrice) {
                newMinSellingPrice = subProduct.getSellingPrice();
            }
            if (newMaxSellingPrice == null || subProduct.getSellingPrice() > newMaxSellingPrice) {
                newMaxSellingPrice = subProduct.getSellingPrice();
            }
        }


        product.setMinMrpPrice(newMinMrpPrice);
        product.setMaxMrpPrice(newMaxMrpPrice);
        product.setMinSellingPrice(newMinSellingPrice);
        product.setMaxSellingPrice(newMaxSellingPrice);


        if (product.getSubProducts().isEmpty()) {
            product.setMinMrpPrice(null);
            product.setMaxMrpPrice(null);
            product.setMinSellingPrice(null);
            product.setMaxSellingPrice(null);
            product.setDiscountPercentage(0);
        } else {
            product.setDiscountPercentage(discountPercentage(product.getMaxMrpPrice(), product.getMinSellingPrice()));
        }

        productRepository.save(product);
    }

    public SubProduct findSubProductById(Long subProductId) {
        return subProductRepository.findById(subProductId).orElseThrow(() -> new AppException("SubProduct not found"));

    }

    public SubProductDto findSubProductByIdT(Long subProductId) {
        SubProduct subProduct = findSubProductById(subProductId);

        return subProductMapper.toDto(subProduct);
    }

    public void restoreStock(Order order) {
        List<SubProduct> subProducts = order.getSellerOrders().stream()
                .flatMap(sellerOrder ->
                        sellerOrder.getOrderItems().stream()
                                .map(orderItem -> {
                                    SubProduct sp = orderItem.getSubProduct();
                                    // Khôi phục tồn kho
                                    sp.setQuantity(sp.getQuantity() + orderItem.getQuantity());
                                    return sp;
                                })
                )
                .toList();

        subProductRepository.saveAll(subProducts);
    }


    public void restoreStock(OrderItem orderItem) {

        SubProduct subProduct = orderItem.getSubProduct();
        subProduct.setQuantity(orderItem.getQuantity() + subProduct.getQuantity());
        subProductRepository.save(subProduct);
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
        if (categories.size() < 3) {
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

    public PageableDto<ProductDto> getAllProduct(
            String category, String brand,
            String colors, String sizes,
            Integer minimumPrice, Integer maximumPrice,
            Integer minimumDiscount,
            String sort,
            String stock,
            Integer pageNumber,
            String search,
            Integer pageSize
    ) {
        Specification<Product> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("totalSubProduct"), 1));

            if (category != null && !category.isEmpty()) {
                Join<Product, Category> joinCategory = root.join("category");
                predicates.add(criteriaBuilder.equal(joinCategory.get("categoryId"), category));
            }
            if (search != null && !search.isEmpty()) {
                var loweredSearch = "%" + search.toLowerCase() + "%";
                predicates.add(
                        criteriaBuilder.or(
                                criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), loweredSearch)
//                                ,criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), loweredSearch)
                        )
                );

            }


            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Pageable pageable;
        Map<String, Sort> sortMap = Map.of(
                "price_low", Sort.by("sellingPrice").ascending(),
                "price_high", Sort.by("sellingPrice").descending(),
                "discount_high", Sort.by("discountPercentage").descending()
        );
        sort = null;
        Sort sortOption = Sort.unsorted();
        pageable = PageRequest.of(pageNumber != null ? (pageNumber - 1) : 0, pageSize, sortOption);

        Page<Product> pageData = productRepository.findAll(specification, pageable);

        Page<ProductDto> dtoPage = pageData.map(productMapper::toProductDto);


        return pageableMapper.toPageableDto(dtoPage);

    }

    public List<Product> getProductsBySellerId(Long sellerId) {
        return productRepository.findBySellerId(sellerId);
    }

}
