package com.ncedu.scooter.product.info.service;

import com.ncedu.scooter.product.info.dao.ProductDAO;
import com.ncedu.scooter.product.info.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDAO productDAO;

    public ProductServiceImpl(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    @Transactional
    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    @Override
    @Transactional
    public List<Product> getProductsCategory(String category) {
        return productDAO.getProductsCategory(category);
    }

    @Override
    @Transactional
    public Product getProduct(int code) {
        return productDAO.getProduct(code);
    }
}
