package com.ncedu.scooter.product.info.dao;

import com.ncedu.scooter.product.info.entity.Products;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductDAO  {
    public List<Products> getAllProducts();
    public List<Products> getProductsCategory(String category);
    public Products getProduct(int code);
}
