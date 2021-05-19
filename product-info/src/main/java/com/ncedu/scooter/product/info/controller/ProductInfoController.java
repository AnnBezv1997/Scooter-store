package com.ncedu.scooter.product.info.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncedu.scooter.product.info.controller.request.ProductRequest;
import com.ncedu.scooter.product.info.controller.request.ProductResponse;
import com.ncedu.scooter.product.info.entity.Category;
import com.ncedu.scooter.product.info.entity.Product;
import com.ncedu.scooter.product.info.service.CategoryService;
import com.ncedu.scooter.product.info.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@Tag(name = "Product information controller for the user.", description = "Get products")
@RequestMapping(value = "/user")
public class ProductInfoController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "Get stock status product", description = "Get Info Product")
    @GetMapping("/stock/status/{id}")
    public Integer getProductStockStatus(@PathVariable(name = "id") int id)  {
        return productService.getProduct(id).getStockStatus().getCount();
    }

    @Operation(summary = "Get product", description = "Get Info Product")
    @GetMapping("/product/{id}")
    public Product getProduct(@PathVariable(name = "id") int id)  {
        return productService.getProduct(id);
    }
    @Operation(summary = "Get page product", description = "")
    @PostMapping("/page/products")
    public ProductResponse getPageProduct(@RequestBody @Valid ProductRequest productRequest)  {
        Page<Product> productPage = productService.pageProduct(productRequest);
        return new ProductResponse(productPage.getTotalPages(), productPage.getContent());

    }

    @Operation(summary = "Get list product", description = "Get Info Product")
    @GetMapping("/products")
    public String getProductsList() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<Product> products = productService.getAllProducts();
        return objectMapper.writeValueAsString(products);
    }


    @Operation(summary = "Get list category", description = "Get Info Category")
    @GetMapping("/categories")
    public String getCategoriesList() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<Category> categories = categoryService.getAllCategory();
        return objectMapper.writeValueAsString(categories);
    }


}
