package com.ncedu.scooter.order.service;

import com.ncedu.scooter.order.entity.UserOrder;
import com.ncedu.scooter.order.repository.UserOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserOrderService {
    @Autowired
    private UserOrderRepository userOrderRepository;
    @Autowired
    private BasketService basketService;

    public boolean createOrder(UserOrder userOrder) {
        UserOrder newUserOrder = new UserOrder();

        newUserOrder.setAddress(userOrder.getAddress());
        newUserOrder.setOrderStatus(userOrder.getOrderStatus());
        newUserOrder.setOrderStatusPay(userOrder.getOrderStatusPay());
        newUserOrder.setDate(userOrder.getDate());
        newUserOrder.setUserId(userOrder.getUserId());
        newUserOrder.setTotalPrice(userOrder.getTotalPrice());
        userOrderRepository.save(newUserOrder);
        basketService.createOrder(newUserOrder);
        return true;
    }

    public boolean updateOrder(UserOrder userOrder) {
        userOrderRepository.save(userOrder);
        return true;
    }

    public List<UserOrder> getUserOrder(Integer userId) {
        return userOrderRepository.findAllByUserId(userId);
    }


}
