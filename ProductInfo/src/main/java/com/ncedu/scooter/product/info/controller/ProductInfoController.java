package com.ncedu.scooter.product.info.controller;

import com.ncedu.scooter.product.info.entity.Product;
import com.ncedu.scooter.product.info.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
@Tag(name = "Product information Controller", description = "Get and Update products")
public class ProductInfoController {
    @Autowired
    private ProductService productService;
    @Operation(description = "Gel all list products")
    @GetMapping("/")
    public List<Product> showAllProduct() {
        return productService.getAllProducts();
    }
    @Operation(description = "Get product by code")
    @GetMapping("/code/{code}")
    public Product getProductByCode(@PathVariable("code") int code) {
        return productService.getProduct(code);
    }
    @Operation(description = "Get products by category")
    @GetMapping("/category/{category}")
    public List<Product> showProductsCategory(@PathVariable(name = "category") String category) {
        return productService.getProductsCategory(category);
    }


}
