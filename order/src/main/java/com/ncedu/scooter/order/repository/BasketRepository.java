package com.ncedu.scooter.order.repository;

import com.ncedu.scooter.order.entity.Basket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BasketRepository extends CrudRepository<Basket, Integer> {
    Basket findAllById(Integer id);
    List<Basket> findAllByUserId(Integer userId);
    List<Basket> findAll();
    Basket findAllByProductId(Integer id);

}
