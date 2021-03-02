package com.ncedu.scooter.product.info.dao;

import com.ncedu.scooter.product.info.entity.Product;

import java.util.List;

public interface ProductDAO  {
    public List<Product> getAllProducts();
    public List<Product> getProductsCategory(String category);
    public Product getProduct(int code);
}
