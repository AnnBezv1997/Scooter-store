package com.ncedu.scooter.product.info.service;

import com.ncedu.scooter.product.info.dao.ProductDAO;
import com.ncedu.scooter.product.info.entity.Products;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDAO productDAO;
    @Override
    @Transactional
    public List<Products> getAllProducts() {
        return productDAO.getAllProducts();
    }

    @Override
    @Transactional
    public List<Products> getProductsCategory(String category) {
        return productDAO.getProductsCategory(category);
    }

    @Override
    @Transactional
    public Products getProduct(int code) {
        return productDAO.getProduct(code);
    }
}
