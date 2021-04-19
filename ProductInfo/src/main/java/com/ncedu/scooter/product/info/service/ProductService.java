package com.ncedu.scooter.product.info.service;

import com.ncedu.scooter.product.info.controller.request.ProductRequest;
import com.ncedu.scooter.product.info.entity.Product;
import com.ncedu.scooter.product.info.exception.ExceptionNotFound;
import com.ncedu.scooter.product.info.repository.ProductRepoPagination;
import com.ncedu.scooter.product.info.repository.ProductRepository;
import com.ncedu.scooter.product.info.repository.StockStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static com.ncedu.scooter.product.info.exception.ExceptionMessage.PRODUCT_NOT_FOUND;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductRepoPagination productRepoPagination;
    @Autowired
    StockStatusRepository stockStatusRepository;

    public Page<Product> pageProduct(ProductRequest productRequest) {
        if (!productRequest.getSearch().isEmpty()) {
            return search(productRequest);
        }
        if (productRequest.getCategorySort() != null) {
            return findAllByCategory(productRequest);
        }
        return productRepoPagination.findAll(createPageRequest(productRequest));
    }

    private Page<Product> search(ProductRequest productRequest) {
        return productRepoPagination.search(productRequest.getSearch(), createPageRequest(productRequest));
    }

    private Page<Product> findAllByCategory(ProductRequest productRequest) {
        return productRepoPagination.findAllByCategoryId(productRequest.getCategorySort(), createPageRequest(productRequest));
    }

    private Pageable createPageRequest(ProductRequest productRequest) {
        if (productRequest.getSortDirection().isEmpty()) {
            return PageRequest.of(productRequest.getNumberPage(), productRequest.getSize());
        } else if (productRequest.getSortDirection().equals("ASC")) {
            return PageRequest.of(productRequest.getNumberPage(), productRequest.getSize(),
                    Sort.by(productRequest.getSortBy()).ascending());
        } else {
            return PageRequest.of(productRequest.getNumberPage(), productRequest.getSize(),
                    Sort.by(productRequest.getSortBy()).descending());
        }
    }



    public ArrayList<Product> getAllProducts() {
        return productRepository.findAll();
    }


    public boolean saveProduct(Product product) {
            Product newProduct = new Product();
            newProduct.setName(product.getName());
            newProduct.setDescription(product.getDescription());
            newProduct.setPrice(product.getPrice());
            newProduct.setImage(product.getImage());
            newProduct.setStockStatus(product.getStockStatus());
            newProduct.setCategory(product.getCategory());
            newProduct.setDiscount(product.getDiscount());
            productRepository.save(newProduct);
            return true;

    }

    public boolean updateProduct(Product product) {
        productRepository.save(product);
        return true;
    }

    public boolean deleteProduct(int id) throws ExceptionNotFound {
        Product product = productRepository.findById(id);
        if (product == null) {
            throw new ExceptionNotFound(PRODUCT_NOT_FOUND);

        } else {
            stockStatusRepository.delete(product.getStockStatus());
            productRepository.delete(product);
            return true;
        }
    }

}
