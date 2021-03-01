package com.ncedu.scooter.product.info.dao;

import com.ncedu.scooter.product.info.entity.Products;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
@Repository
public class ProductDAOImpl implements ProductDAO  {
    @Autowired
    private EntityManager entityManager;
    @Override
   public List<Products> getAllProducts() {

       Session session = entityManager.unwrap(Session.class);
       Query<Products> query = session.createQuery("From products",Products.class);
       List<Products> allProducts = query.getResultList();
        return allProducts;
    }
    @Override
    public Products getProduct(int code) {
        Session session = entityManager.unwrap(Session.class);
        Products products = session.get(Products.class,code);
        return products;
    }
    @Override
    public List<Products> getProductsCategory(String category) {

        Session session = entityManager.unwrap(Session.class);
        Query<Products> query = session.createQuery("From products where category =: productsCategory",Products.class);
        query.setParameter("productsCategory",category);
        List<Products> productsCategory = query.getResultList();
        return productsCategory;

    }
}
