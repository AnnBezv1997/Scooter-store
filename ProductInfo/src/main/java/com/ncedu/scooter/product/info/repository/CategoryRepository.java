package com.ncedu.scooter.product.info.repository;

import com.ncedu.scooter.product.info.entity.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer> {
    Category findById(int id);

    ArrayList<Category> findAll();
}
