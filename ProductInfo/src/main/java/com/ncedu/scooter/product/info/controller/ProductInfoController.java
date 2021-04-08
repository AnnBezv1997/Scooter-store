package com.ncedu.scooter.product.info.controller;

import com.ncedu.scooter.product.info.entity.Category;
import com.ncedu.scooter.product.info.entity.Product;
import com.ncedu.scooter.product.info.exception.CategoryNotFound;
import com.ncedu.scooter.product.info.exception.ProductNotFound;
import com.ncedu.scooter.product.info.service.CategoryService;
import com.ncedu.scooter.product.info.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@Tag(name = "Product information Controller", description = "Get and Update products")
public class ProductInfoController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "Get product", description = "Get Info Product")
    @GetMapping("/product/{id}")
    public Product getProduct(@PathVariable("id") int id) throws ProductNotFound {
        try {
            return productService.findById(id);
        } catch (ProductNotFound e) {
            e.getMessage();
            return null;
        }
    }

    @Operation(summary = "Get list product", description = "Get Info Product")
    @GetMapping("/products")
    public ArrayList<Product> getProductsList() {
        return productService.getAllProducts();
    }


    @Operation(summary = "Get category", description = "Get Catefory")
    @GetMapping("/cagegory/{id}")
    public Category getCategory(@PathVariable("id") int id) throws CategoryNotFound {
        try {
            return categoryService.findById(id);
        } catch (CategoryNotFound e) {
            e.getMessage();
            return null;
        }
    }

    @Operation(summary = "Get list category", description = "Get Info Category")
    @GetMapping("/categories")
    public ArrayList<Category> getCategoriesList() {
        return categoryService.getAllCategory();
    }
    //дальше будут методы для админа удаление добавлени и езменение в Service прописаны

}
