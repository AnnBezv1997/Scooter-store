package com.ncedu.scooter.product.info.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncedu.scooter.product.info.controller.request.ProductRequest;
import com.ncedu.scooter.product.info.controller.request.ProductResponse;
import com.ncedu.scooter.product.info.entity.Category;
import com.ncedu.scooter.product.info.entity.Discount;
import com.ncedu.scooter.product.info.entity.Product;
import com.ncedu.scooter.product.info.entity.StockStatus;
import com.ncedu.scooter.product.info.exception.ExceptionNotFound;
import com.ncedu.scooter.product.info.service.CategoryService;
import com.ncedu.scooter.product.info.service.DiscountService;
import com.ncedu.scooter.product.info.service.ProductService;
import com.ncedu.scooter.product.info.service.StockStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@Tag(name = "Product Information controller for the administrator.", description = "Get and Update products")
@RequestMapping(value = "/admin")
public class ProductInfoControllerAdmin {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DiscountService discountService;
    @Autowired
    private StockStatusService stockStatusService;

    @Operation(summary = "Get page product. Admin.", description = "")
    @PostMapping("/page/products")
    public ProductResponse getPageProductAdmin(@RequestBody @Valid ProductRequest productRequest) {
        Page<Product> productPage = productService.pageProduct(productRequest);
        return new ProductResponse(productPage.getTotalPages(), productPage.getContent());

    }

    @Operation(summary = "Get list category.Admin", description = "Get Info Category")
    @GetMapping("/categories")
    public String getCategoriesListAdmin() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<Category> categories = categoryService.getAllCategory();
        return objectMapper.writeValueAsString(categories);
    }

    @Operation(summary = "Get list category.Admin", description = "Get Info Category")
    @GetMapping("/discount")
    public String getDiscountListAdmin() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<Discount> discounts = discountService.getAllDiscount();
        return objectMapper.writeValueAsString(discounts);
    }



    @Operation(summary = "Admin create product.", description = "")
    @PostMapping("/add/product")
    public boolean createProduct(@RequestBody @Valid Product product) {
        return productService.saveProduct(product);

    }

    @Operation(summary = "Admin create category", description = "")
    @PostMapping("/add/category")
    public boolean createCategory(@RequestBody @Valid Category category) {
        return categoryService.saveCategory(category);

    }

    @Operation(summary = "Admin create discount", description = "")
    @PostMapping("/add/discount")
    public boolean createDiscount(@RequestBody @Valid Discount discount) throws JsonProcessingException {
        return discountService.saveDiscount(discount);

    }

    @Operation(summary = "Admin create stock status", description = "")
    @PostMapping("/add/status")
    public boolean createStockStatus(@RequestBody @Valid StockStatus stockStatus) {
        return stockStatusService.saveStockStatus(stockStatus);

    }

    @Operation(summary = "Admin delete product.", description = "")
    @PostMapping("/delete/product")
    public boolean deleteProduct(@RequestBody @Valid Product product) throws ExceptionNotFound {
        return productService.deleteProduct(product.getId());

    }

    @Operation(summary = "Admin delete category.", description = "")
    @PostMapping("/delete/category")
    public boolean deleteCategory(@RequestBody @Valid Category category) throws ExceptionNotFound {
        return categoryService.deleteCategory(category.getId());

    }

    @Operation(summary = "Admin delete discount.", description = "")
    @PostMapping("/delete/discount")
    public boolean deleteDiscount(@RequestBody @Valid Discount discount) throws ExceptionNotFound {
        return discountService.deleteDiscount(discount.getId());

    }

    @Operation(summary = "Admin update product.", description = "")
    @PostMapping("/update/product")
    public boolean updateProduct(@RequestBody @Valid Product product) {
        return productService.updateProduct(product);

    }

    @Operation(summary = "Admin update category.", description = "")
    @PostMapping("/update/category")
    public boolean updateCategory(@RequestBody @Valid Category category) throws ExceptionNotFound {
        return categoryService.updateCategory(category);

    }

    @Operation(summary = "Admin update discount.", description = "")
    @PostMapping("/update/discount")
    public boolean updateDiscount(@RequestBody @Valid Discount discount) throws ExceptionNotFound {
        return discountService.updateDiscount(discount);

    }

    @Operation(summary = "Admin update stock status.", description = "")
    @PostMapping("/update/status")
    public boolean updateStockStatus(@RequestBody @Valid StockStatus stockStatus) {
        return stockStatusService.updateStockStatus(stockStatus);

    }



    @PostMapping("/add")
    public boolean create(@RequestBody @Valid String o) throws JsonProcessingException{
        ObjectMapper objectMapper = new ObjectMapper();
        return false;

    }
}
