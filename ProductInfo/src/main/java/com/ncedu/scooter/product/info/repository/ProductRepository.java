package com.ncedu.scooter.product.info.repository;

import com.ncedu.scooter.product.info.entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {
    Product findById(int id);
    ArrayList<Product> findAll();

}
