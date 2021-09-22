package com.ncedu.scooter.product.info.service;

import com.ncedu.scooter.product.info.entity.Category;
import com.ncedu.scooter.product.info.entity.Product;
import com.ncedu.scooter.product.info.exception.ExceptionCategory;
import com.ncedu.scooter.product.info.exception.ExceptionNotFound;
import com.ncedu.scooter.product.info.repository.CategoryRepository;
import com.ncedu.scooter.product.info.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.ncedu.scooter.product.info.exception.ExceptionMessage.CATEGORY_NOT_FOUND;
import static com.ncedu.scooter.product.info.exception.ExceptionMessage.CATEGORY_PARENT;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductRepository productRepository;

    public ArrayList<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    public boolean saveCategory(Category category){
        Category newCategory = new Category();
        newCategory.setName(category.getName());
        newCategory.setDescription(category.getDescription());
        newCategory.setCategoryParent(category.getCategoryParent());
        categoryRepository.save(newCategory);
        return true;


    }

    public boolean updateCategory(Category category) throws ExceptionNotFound, ExceptionCategory {
        Category categ = categoryRepository.findById(category.getId().intValue());
        if (categ == null) {
            throw new ExceptionNotFound(CATEGORY_NOT_FOUND);
        }else if (category.getCategoryParent() == null) {
            categoryRepository.save(category);
            return true;
        }else if(category.equals(category.getCategoryParent().getCategoryParent())){
            throw new ExceptionCategory(CATEGORY_PARENT);
        } else{
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
            List<Category> categoryList = categoryRepository.findAll();
            for (Product p : productList) {
                p.setCategory(null);
                productRepository.save(p);
            }
            for (Category category : categoryList){
                if(category.getCategoryParent() != null && category.getCategoryParent().getId() == id){
                    category.setCategoryParent(null);
                }
            }
            categoryRepository.delete(c);
            return true;
        }
    }

}
