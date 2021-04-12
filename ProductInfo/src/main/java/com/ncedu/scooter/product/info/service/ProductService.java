package com.ncedu.scooter.product.info.service;

import com.ncedu.scooter.product.info.entity.Discount;
import com.ncedu.scooter.product.info.entity.Product;
import com.ncedu.scooter.product.info.exception.ProductNotFound;
import com.ncedu.scooter.product.info.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static com.ncedu.scooter.product.info.exception.ExceptionMessage.PRODUCT_NOT_FOUND;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    public Product findById(int id) throws ProductNotFound {
        Product product = productRepository.findById(id);
        if (product != null) {
            return product;
        } else {
            throw new ProductNotFound(PRODUCT_NOT_FOUND);
        }
    }

    public ArrayList<Product> getAllPriducts() {
        return productRepository.findAllProducts();
    }

    public boolean saveProduct(Product product) {
        Product p = productRepository.findById((int) product.getId());
        if (p != null) {
            return false;
        } else {
            productRepository.save(product);
            return true;
        }
    }

    public boolean updateProduct(Product product) {
        Product p = new Product(product.getId(), product.getName(), product.getDescription(),
                product.getPrice(), product.getImage(), product.getStockStatus(),
                product.getCategory(), product.getDiscount());
        productRepository.save(p);
        return true;
    }

    public boolean deleteProduct(int id) {
        Product product = productRepository.findById(id);
        if (product != null) {
            return false;
        } else {
            productRepository.delete(product);
            return true;
        }
    }

    public boolean addUpdateDiscount(Discount discount, Product product) {
        Product p = productRepository.findById((int) product.getId());
        if (p != null) {
            p.setDiscount(discount);
            productRepository.save(p);
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteDiscount(Product product) {
        Product p = productRepository.findById((int) product.getId());
        if (p != null) {
            p.setDiscount(null);
            productRepository.save(p);
            return true;
        } else {
            return false;
        }
    }
}
