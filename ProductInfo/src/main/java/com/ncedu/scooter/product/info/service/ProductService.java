package com.ncedu.scooter.product.info.service;

import com.ncedu.scooter.product.info.entity.Product;
import com.ncedu.scooter.product.info.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsCategory(String category) {
        return productRepository.findAllByCategory(category);

    }

    public Product getProduct(int code) {
        return productRepository.findByCode(code);
    }

    public void saveProduct(Product product) {
        productRepository.save(product);
    }
    public void updateProduct(int code, Product product) {
        Product oldProduct = productRepository.findByCode(code);
        oldProduct.setName(product.getName());
        oldProduct.setPrice(product.getPrice());
        oldProduct.setCategory(product.getCategory());
        oldProduct.setDescription(product.getDescription());
        oldProduct.setManufacturer(product.getManufacturer());
    }
}
