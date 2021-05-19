package com.ncedu.scooter.product.info.repository;

import com.ncedu.scooter.product.info.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepoPagination extends PagingAndSortingRepository<Product, Integer> {

    @Query("SELECT p FROM product p WHERE fts(:name) = true OR "
            + "p IN (SELECT prod.id FROM product prod WHERE category_id "
            + "IN (SELECT c.id FROM category c WHERE fts(:name) = true)) ")
    Page<Product> search(@Param("name") String name, Pageable pageable);

    Page<Product> findAllByCategoryId(Integer id , Pageable pageable);
}
