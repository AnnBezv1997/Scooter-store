package com.ncedu.scooter.product.info.service;

import com.ncedu.scooter.product.info.entity.Products;

import java.util.List;

public interface ProductService {

    public List<Products> getAllProducts();
    public List<Products> getProductsCategory(String category);
    public Products getProduct(int code);
}
