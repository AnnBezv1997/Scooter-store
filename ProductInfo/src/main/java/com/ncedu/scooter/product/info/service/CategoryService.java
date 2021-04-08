package com.ncedu.scooter.product.info.service;

import com.ncedu.scooter.product.info.entity.Category;
import com.ncedu.scooter.product.info.exception.CategoryNotFound;
import com.ncedu.scooter.product.info.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static com.ncedu.scooter.product.info.exception.ExceptionMessage.CATEGORY_NOT_FOUND;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public Category findById(int id) throws CategoryNotFound {
        Category category = categoryRepository.findById(id);
        if (category != null) {
            return category;
        } else {
            throw new CategoryNotFound(CATEGORY_NOT_FOUND);
        }
    }

    public ArrayList<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    public boolean saveCategory(Category category) {
        Category c = categoryRepository.findById((int) category.getId());
        if (c != null) {
            return false;
        } else {
            categoryRepository.save(c);
            return true;
        }
    }

    public boolean updateCategory(Category category) {
        Category c = new Category(category.getId(), category.getName(), category.getDescription(),
                category.getCategoryParent());
        categoryRepository.save(c);
        return true;
    }

    public boolean deleteCategory(int id) {
        Category c = categoryRepository.findById(id);
        if (c != null) {
            return false;
        } else {
            categoryRepository.delete(c);
            return true;
        }
    }

}
