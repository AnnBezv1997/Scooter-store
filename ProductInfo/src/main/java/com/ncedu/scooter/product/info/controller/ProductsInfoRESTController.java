package com.ncedu.scooter.product.info.controller;

import com.ncedu.scooter.product.info.entity.Products;
import com.ncedu.scooter.product.info.service.ProductService;
import com.ncedu.scooter.product.info.service.SearchProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class ProductsInfoRESTController {
    @Autowired
   private ProductService productService;
    @Autowired
    SearchProductService searchProductService;
    @GetMapping("/products")
    public List<Products> showAllProduct(){
        List<Products> allProducts = productService.getAllProducts();
        return allProducts;
    }

    @GetMapping("/code/{code}")
    public Products showProductCode( @PathVariable("code") int code){
        Products product = productService.getProduct(code);
        return product;
    }

    @GetMapping("/products/category/{category}")
    public List<Products> showProductsCategory( @PathVariable(name = "category") String category){
        List<Products> productsCategory = productService.getProductsCategory(category);
        return productsCategory;
    }

    @GetMapping("/search/{name}")
    public List<Products> showProductsName(@PathVariable(name="name") String name ){
        List<Products> results =searchProductService.simpleSearchProduct(name);
        return results;
    }
}
