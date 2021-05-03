package com.ncedu.scooter.order.repository;

import com.ncedu.scooter.order.entity.UserOrder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserOrderRepository extends CrudRepository<UserOrder, Integer> {
    List<UserOrder> findAllByUserId(Integer id);
    UserOrder findAllById(Integer id);

}
