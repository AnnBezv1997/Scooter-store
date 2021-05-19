package com.ncedu.scooter.order.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncedu.scooter.order.controller.request.PriceResponse;
import com.ncedu.scooter.order.controller.request.ProductRequest;
import com.ncedu.scooter.order.entity.Basket;
import com.ncedu.scooter.order.entity.UserOrder;
import com.ncedu.scooter.order.exeption.ExceptionProductQuantity;
import com.ncedu.scooter.order.service.BasketService;
import com.ncedu.scooter.order.service.UserOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Tag(name = "Product information controller for the user.", description = "Get products")

public class OrderController {
    @Autowired
    private BasketService basketService;
    @Autowired
    private UserOrderService userOrderService;

    @Operation(summary = "Get product basket", description = "")
    @PostMapping("/basket/products")
    public String getProduct(@RequestBody @Valid ProductRequest productRequest) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Basket> productBasket = basketService.getProduct(productRequest);
        return objectMapper.writeValueAsString(productBasket);

    }

    @Operation(summary = "Get user order", description = "")
    @PostMapping("/")
    public String getUserOrder(@RequestBody @Valid Integer userId) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<UserOrder> userOrderList = userOrderService.getUserOrder(userId);
        return objectMapper.writeValueAsString(userOrderList);

    }

    @Operation(summary = "Add product to basket.", description = "")
    @PostMapping("/add/product")
    public boolean addProduct(@RequestBody @Valid Basket basket) throws ExceptionProductQuantity {
        return basketService.addProduct(basket);
    }

    @Operation(summary = "Delete product to basket.", description = "")
    @PostMapping("/delete/product")
    public boolean deleteProduct(@RequestBody @Valid Basket basket) {
        return basketService.deleteProduct(basket);
    }

    @Operation(summary = "Update count product to basket.", description = "")
    @PostMapping("/update/count")
    public boolean updateCountProduct(@RequestBody @Valid Basket basket) throws ExceptionProductQuantity {
        return basketService.updateCountProduct(basket);
    }

    @Operation(summary = "Get total price product", description = "")
    @GetMapping("/total/price/{id}")
    public PriceResponse getTotalPriceAndTotalDiscount(@PathVariable(name = "id") int userId) {
        return basketService.totalPriceAndTotalDickount(userId);
    }

    @Operation(summary = "Create order.", description = "")
    @PostMapping("/create")
    public boolean createOrder(@RequestBody @Valid UserOrder userOrder) {
        return userOrderService.createOrder(userOrder);
    }

    @Operation(summary = "Delete order.", description = "")
    @PostMapping("/delete")
    public boolean deleteOrder(@RequestBody @Valid ProductRequest productRequest) throws ExceptionProductQuantity {
        return basketService.deleteOrder(productRequest);
    }

    @Operation(summary = "Update order.", description = "")
    @PostMapping("/update")
    public boolean updateOrder(@RequestBody @Valid UserOrder userOrder) {
        return userOrderService.updateOrder(userOrder);
    }


}
