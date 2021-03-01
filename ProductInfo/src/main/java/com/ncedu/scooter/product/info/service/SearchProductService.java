package com.ncedu.scooter.product.info.service;

import com.ncedu.scooter.product.info.dao.ProductDAO;
import com.ncedu.scooter.product.info.entity.Products;
import org.apache.lucene.search.Query;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;

import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

@Service
public class SearchProductService {
//вот здесь ошибок не выдвает, но ничего и не ищет дает пустой массив

   @Autowired
    private EntityManagerFactory entityManagerFactory;
   @Transactional
    public List<Products> simpleSearchProduct(String name){
      EntityManager em = entityManagerFactory.createEntityManager();
       FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);

       QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder()
               .forEntity(Products.class)
               .get();
       Query luceneQuery = queryBuilder
               .keyword()
               .onFields("name")
               .matching(name)
               .createQuery();
       FullTextQuery query = fullTextEntityManager.createFullTextQuery(luceneQuery, Products.class);
      return query.getResultList();

   }

}
