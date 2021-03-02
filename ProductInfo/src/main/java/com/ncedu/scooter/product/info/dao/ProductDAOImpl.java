package com.ncedu.scooter.product.info.dao;

import com.ncedu.scooter.product.info.entity.Product;
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
   public List<Product> getAllProducts() {

       Session session = entityManager.unwrap(Session.class);
       Query<Product> query = session.createQuery("From product",Product.class);
       List<Product> allProducts = query.getResultList();
        return allProducts;
    }
    @Override
    public Product getProduct(int code) {
        Session session = entityManager.unwrap(Session.class);
        Product product = session.get(Product.class,code);
        return product;
    }
    @Override
    public List<Product> getProductsCategory(String category) {

        Session session = entityManager.unwrap(Session.class);
        Query<Product> query = session.createQuery("From product where category =: productsCategory",Product.class);
        query.setParameter("productsCategory",category);
        List<Product> productCategory = query.getResultList();
        return productCategory;

    }
}
