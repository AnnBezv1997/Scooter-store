package com.ncedu.scooter.product.info.controller;

import com.ncedu.scooter.product.info.entity.Product;
import com.ncedu.scooter.product.info.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class ProductsInfoRESTController {
    @Autowired
   private ProductService productService;

    @GetMapping("/")
    public List<Product> showAllProduct(){
        List<Product> allProducts = productService.getAllProducts();
        return allProducts;
    }

    @GetMapping("/code/{code}")
    public Product showProductCode(@PathVariable("code") int code){
        Product product = productService.getProduct(code);
        return product;
    }

    @GetMapping("/category/{category}")
    public List<Product> showProductsCategory(@PathVariable(name = "category") String category){
        List<Product> productCategory = productService.getProductsCategory(category);
        return productCategory;
    }

}
