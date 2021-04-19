package com.ncedu.scooter.product.info.service;

import com.ncedu.scooter.product.info.entity.Category;
import com.ncedu.scooter.product.info.entity.Product;
import com.ncedu.scooter.product.info.exception.ExceptionNotFound;
import com.ncedu.scooter.product.info.repository.CategoryRepository;
import com.ncedu.scooter.product.info.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.ncedu.scooter.product.info.exception.ExceptionMessage.CATEGORY_NOT_FOUND;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductRepository productRepository;

    public ArrayList<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    public boolean saveCategory(Category category) {
        Category newCategory = new Category();
        newCategory.setName(category.getName());
        newCategory.setDescription(category.getDescription());
        newCategory.setCategoryParent(category.getCategoryParent());
        categoryRepository.save(newCategory);
        return true;
    }

    public boolean updateCategory(Category category) throws ExceptionNotFound {
        Category categ = categoryRepository.findById(category.getId().intValue());
        if (categ == null) {
            throw new ExceptionNotFound(CATEGORY_NOT_FOUND);
        } else {
            categoryRepository.save(category);
            return true;
        }
    }

    public boolean deleteCategory(int id) throws ExceptionNotFound {
        Category c = categoryRepository.findById(id);
        if (c == null) {
            throw new ExceptionNotFound(CATEGORY_NOT_FOUND);
        } else {
            List<Product> productList = productRepository.findByCategoryId(id);
            for (Product p : productList) {
                p.setDiscount(null);
                productRepository.save(p);
            }
            categoryRepository.delete(c);
            return true;
        }
    }

}
