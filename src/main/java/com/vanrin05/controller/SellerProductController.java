package com.vanrin05.controller;

import com.vanrin05.dto.request.CreateProductReq;
import com.vanrin05.dto.request.UpdateProductReq;
import com.vanrin05.model.Product;
import com.vanrin05.service.ProductService;
import com.vanrin05.service.SellerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sellers/products")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SellerProductController {

    ProductService productService;
    SellerService sellerService;

    @GetMapping
    public ResponseEntity<List<Product>> getProductsBySellerId(@RequestHeader("Authorization") String jwt){
        return ResponseEntity.ok(productService.getProductsBySellerId(sellerService.getSellerProfile(jwt).getId()));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestHeader("Authorization") String jwt, @RequestBody CreateProductReq req){
        return ResponseEntity.ok(productService.createProduct(req, jwt));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@RequestHeader("Authorization") String jwt, @PathVariable("productId") Long productId){
        productService.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable("productId") Long productId,
                                                 @RequestBody UpdateProductReq req){
        return ResponseEntity.ok(productService.updateProduct(productId, req));
    }

}
