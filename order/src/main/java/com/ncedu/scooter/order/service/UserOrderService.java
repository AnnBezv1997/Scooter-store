package com.ncedu.scooter.order.service;

import com.ncedu.scooter.order.entity.OrderStatus;
import com.ncedu.scooter.order.entity.OrderStatusPay;
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

    public boolean createOrder(UserOrder userOrder, String token) {
        UserOrder newUserOrder = new UserOrder();
        newUserOrder.setUserId(userOrder.getUserId());
        newUserOrder.setAddress(userOrder.getAddress());
        newUserOrder.setOrderStatus(userOrder.getOrderStatus());
        newUserOrder.setOrderStatusPay(userOrder.getOrderStatusPay());
        newUserOrder.setDate(userOrder.getDate());
        newUserOrder.setTotalPrice(userOrder.getTotalPrice());
        userOrderRepository.save(newUserOrder);
        if(basketService.createOrder(newUserOrder, token)){
            return true;
        }else {
            userOrderRepository.delete(newUserOrder);
            return false;
        }

    }

    public boolean updateOrder(UserOrder userOrder,String token) {
        UserOrder orderOld = userOrderRepository.findAllById(userOrder.getId());
        if(userOrder.getOrderStatusPay().equals(OrderStatusPay.YES) && orderOld.getOrderStatusPay().equals(OrderStatusPay.NOT)){
            if(basketService.checkProductPay(userOrder.getUserId(),userOrder.getId(),token)){
                userOrderRepository.save(userOrder);
                return true;
            }
            return false;

        }else if(orderOld.getOrderStatusPay() == userOrder.getOrderStatusPay() ||  userOrder.getOrderStatus().equals(OrderStatus.CANCELED)){
            userOrderRepository.save(userOrder);
            return true;
        }
        return true;
    }

    public List<UserOrder> getUserOrder(Integer userId) {
        return userOrderRepository.findAllByUserId(userId);
    }


}
