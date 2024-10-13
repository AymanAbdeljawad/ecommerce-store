package com.product_service.controller;

import com.product_service.dto.*;
import com.product_service.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/products")
@Validated
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/createProduct")
    public ResponseEntity<ResponseProduct> createProduct(@Valid @RequestBody RequestProduct requestProduct) {
        ResponseProduct responseProduct = productService.createProduct(requestProduct);
        return ResponseEntity.ok(responseProduct);
    }


    @PostMapping("/getAllProducts")

    public ResponseEntity<ResponseAllProduct> getAllProducts() {
        ResponseAllProduct responseAllProduct = productService.getAllProducts();
        return ResponseEntity.ok(responseAllProduct);
    }

    @PostMapping("/getProductById")
    public ResponseEntity<ResponseProduct> getProductById(@Valid @RequestBody RequestProductIdDTO requestProductIdDTO) {
        ResponseProduct responseProduct = productService.getProductById(requestProductIdDTO);
        return ResponseEntity.ok(responseProduct);
    }

    @PostMapping("/updateProduct")
    public ResponseEntity<ResponseProduct> updateProduct(@Valid @RequestBody RequestProduct requestProduct) {
        ResponseProduct responseProduct = productService.updateProduct(requestProduct);
        return ResponseEntity.ok(responseProduct);
    }

    @PostMapping("/updateQuantityList")
    public ResponseEntity<ResponseProduct> updateQuantityList(@Valid @RequestBody RequestUpdateQuantity requestUpdateQuantity) {
        ResponseProduct responseProduct = productService.updateQuantityList(requestUpdateQuantity);
        return ResponseEntity.ok(responseProduct);
    }


    @PostMapping("/deleteProduct")
    public ResponseEntity<ResponseProduct> deleteProduct(@Valid @RequestBody RequestProductIdDTO requestProductIdDTO) {
        ResponseProduct responseProduct = productService.deleteProduct(requestProductIdDTO);
        return ResponseEntity.ok(responseProduct);
    }
}
