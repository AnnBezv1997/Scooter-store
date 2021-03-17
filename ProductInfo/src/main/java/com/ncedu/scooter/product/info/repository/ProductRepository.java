package com.ncedu.scooter.product.info.repository;

import com.ncedu.scooter.product.info.entity.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Integer> {
    Product findByCode(int code);

    List<Product> findAllByCategory(String category);

    List<Product> findAll();


}
