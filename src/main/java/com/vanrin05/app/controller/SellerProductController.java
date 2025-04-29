package com.vanrin05.app.controller;

import com.vanrin05.app.dto.request.CreateProductReq;
import com.vanrin05.app.dto.request.CreateSubProductReq;
import com.vanrin05.app.dto.request.UpdateProductReq;
import com.vanrin05.app.dto.request.UpdateSubProductReq;
import com.vanrin05.app.model.product.Product;
import com.vanrin05.app.model.product.SubProduct;
import com.vanrin05.app.service.impl.ProductService;
import com.vanrin05.app.service.impl.SellerService;
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
    public ResponseEntity<List<Product>> getProductsBySellerId(@RequestHeader("Authorization") String jwt) {
        return ResponseEntity.ok(productService.getProductsBySellerId(sellerService.getSellerProfile(jwt).getId()));
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestHeader("Authorization") String jwt, @RequestBody CreateProductReq req) {
        return ResponseEntity.ok(productService.createProduct(req, jwt));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@RequestHeader("Authorization") String jwt, @PathVariable("productId") Long productId) {
        productService.deleteProduct(productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable("productId") Long productId,
                                                 @RequestBody UpdateProductReq req) {
        return ResponseEntity.ok(productService.updateProduct(productId, req));
    }

    @PostMapping("/sub/{productId}")
    public ResponseEntity<SubProduct> createSubProduct(@PathVariable Long productId, @RequestBody CreateSubProductReq req, @RequestHeader("Authorization") String token) {
        return ResponseEntity.ok().body(
                productService.addSubProductToProduct(productId, req, token)
        );
    }

    @DeleteMapping("/sub/{productId}/delete/{subProductId}")
    public ResponseEntity<Void> deleteSubProduct(@PathVariable Long productId,@RequestHeader("Authorization") String token, @PathVariable Long subProductId) {
        productService.deleteSubProduct(productId, token, subProductId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/sub/{subProductId}")
    public ResponseEntity<SubProduct> updateSubProduct(@PathVariable Long subProductId, @RequestBody UpdateSubProductReq req,
                                                       @RequestHeader("Authorization") String jwt) {
        return ResponseEntity.ok().body(
                productService.updateSubProduct(subProductId, req, jwt)
        );
    }

}