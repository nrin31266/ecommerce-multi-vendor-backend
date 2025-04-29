package com.vanrin05.app.controller;

import com.vanrin05.app.dto.ProductDto;
import com.vanrin05.app.dto.SubProductDto;
import com.vanrin05.app.dto.request.CreateSubProductReq;
import com.vanrin05.app.model.product.Product;
import com.vanrin05.app.model.product.SubProduct;
import com.vanrin05.app.service.impl.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductController {
    ProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.findProductById(productId));
    }

    @GetMapping("/sub-product/{subProductId}")
    public ResponseEntity<SubProductDto> getSubProductById(@PathVariable Long subProductId) {
        return ResponseEntity.ok(productService.findSubProductByIdT(subProductId));
    }



    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam(required = false) String query) {
        return ResponseEntity.ok(productService.searchProduct(query));
    }

    @GetMapping
    public ResponseEntity<Page<ProductDto>> getAllProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String colors,
            @RequestParam(required = false) String sizes,
            @RequestParam(required = false) Integer minimumPrice,
            @RequestParam(required = false) Integer maximumPrice,
            @RequestParam(required = false) Integer minimumDiscount,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String stock,
            @RequestParam(defaultValue = "1", required = false) Integer pageNumber,
            @RequestParam(required = false) String search
    ) {

        return ResponseEntity.ok(productService.getAllProduct(category, brand, colors, sizes, minimumPrice, maximumPrice, minimumDiscount, sort, stock, pageNumber, search));
    }


}
