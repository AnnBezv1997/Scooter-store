package com.ncedu.scooter.order.service;

import com.ncedu.scooter.order.controller.request.PriceResponse;
import com.ncedu.scooter.order.controller.request.Product;
import com.ncedu.scooter.order.controller.request.ProductRequest;
import com.ncedu.scooter.order.entity.Basket;
import com.ncedu.scooter.order.entity.OrderStatusPay;
import com.ncedu.scooter.order.entity.UserOrder;
import com.ncedu.scooter.order.exeption.ExceptionProductQuantity;
import com.ncedu.scooter.order.repository.BasketRepository;
import com.ncedu.scooter.order.repository.UserOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;

import static com.ncedu.scooter.order.exeption.ExceptionMessage.PRODUCT_QUANTITY_ERROR;

@Service
public class BasketService {
    @Autowired
    private BasketRepository basketRepository;
    @Autowired
    private UserOrderRepository orderRepository;
    @Autowired
    private DiscoveryClient discoveryClient;


    public boolean addProduct(Basket basket, String token) throws ExceptionProductQuantity {
      if(getProduct(basket.getProductId(), token) != null){
          Integer countProduct = checkProductStockStatus(basket.getProductId(), token);
          Basket b = searchBasket(basket);
          if (b != null) {
              if ((b.getCountProduct() + basket.getCountProduct()) <= countProduct) {
                  b.setCountProduct(b.getCountProduct() + basket.getCountProduct());
                  basketRepository.save(b);
                  return true;
              } else {
                  throw new ExceptionProductQuantity(PRODUCT_QUANTITY_ERROR);
              }

          } else {
              if (basket.getCountProduct() <= countProduct) {
                  addNewProduct(basket);
                  return true;
              } else {
                  throw new ExceptionProductQuantity(PRODUCT_QUANTITY_ERROR);
              }

          }
      }else return false;

    }

    private Basket searchBasket(Basket basket) {
        List<Basket> basketList = basketListNoOrder(basket.getUserId());
        for (Basket basket1 : basketList) {
            if (basket1.getProductId().equals(basket.getProductId())) {
                return basket1;
            }
        }
        return null;
    }

    private Integer checkProductStockStatus(int id, String token) {
        List<ServiceInstance> instances = discoveryClient.getInstances("PRODUCT_INFO");
        ServiceInstance serviceInstance = instances.get(0);
        String baseUrl = serviceInstance.getUri().toString();


        baseUrl = baseUrl + "/user/stock/status/" + id;

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<?> requestBody = new HttpEntity<>( headers);
        return restTemplate.exchange(baseUrl, HttpMethod.GET, requestBody, Integer.class).getBody();

    }

    private void addNewProduct(Basket basket) {

        Basket b = new Basket();
        b.setUserId(basket.getUserId());
        b.setProductId(basket.getProductId());
        b.setUserOrder(basket.getUserOrder());
        b.setPrice(basket.getPrice());
        b.setCountProduct(basket.getCountProduct());
        b.setDate(basket.getDate());
        basketRepository.save(b);
    }

    public boolean deleteProduct(Basket basket) {
        basketRepository.delete(basket);
        return true;
    }

    public boolean createOrder(UserOrder userOrder, String token) {
        List<Basket> basketList = basketListNoOrder(userOrder.getUserId());

        if(userOrder.getOrderStatusPay().equals(OrderStatusPay.YES)){
            for(Basket basket : basketList){
                if(getProduct(basket.getProductId(),token) == null){
                    return false;
                }else {
                    basket.setUserOrder(userOrder);
                    basketRepository.save(basket);
                }
            }
        }else if(userOrder.getOrderStatusPay().equals(OrderStatusPay.NOT)) {
            for (Basket b : basketList) {
                b.setUserOrder(userOrder);
                basketRepository.save(b);
            }
            return  true;
        }
        return  true;
    }

    public boolean deleteOrder(ProductRequest productRequest, String token) throws ExceptionProductQuantity {
        List<Basket> basketList = basketListWhithOrder(productRequest.getUserId(), productRequest.getOrderId());
        System.out.println(basketList);
        for (Basket b : basketList) {
            if(getProduct(b.getProductId(), token) != null){
                Basket basket = new Basket();
                basket.setCountProduct(b.getCountProduct());
                basket.setProductId(b.getProductId());
                basket.setPrice(b.getPrice());
                basket.setDate(b.getDate());
                basket.setUserId(b.getUserId());
                addProduct(basket, token);
            }
        }
        return true;
    }

    public boolean updateCountProduct(Basket basket, String token) throws ExceptionProductQuantity {
        Integer countProduct = checkProductStockStatus(basket.getProductId(), token);
        Basket b = basketRepository.findAllById(basket.getId());
        if (basket.getCountProduct() > countProduct) {
            throw new ExceptionProductQuantity(PRODUCT_QUANTITY_ERROR);
        } else {
            b.setCountProduct(basket.getCountProduct());
            basketRepository.save(b);
            return true;
        }
    }

    public PriceResponse totalPriceAndTotalDickount(Integer userId, String token) {
        List<Basket> basketList = basketListNoOrder(userId);
        List<Product> productList = listProductBasket(basketList, token);
        double totalPrice = 0;
        double totalDiscount = 0;
        for (Basket b : basketList) {
            if(getProduct(b.getProductId(), token) != null){
                double p = b.getCountProduct() * b.getPrice().doubleValue();
                totalPrice += p;
            }

        }

        for(Basket b : basketList){
            for(Product p : productList){
                if(p != null && b.getProductId() == p.getId() && p.getDiscount() != null){
                    if(p.getDiscount().getDiscountType().toString().equals("ABSOLUTE")){
                        totalDiscount += p.getDiscount().getValue().doubleValue()*b.getCountProduct();
                    }else {
                        totalDiscount += p.getPrice().multiply(new BigDecimal(p.getDiscount().getValue().doubleValue() / 100)).doubleValue()*b.getCountProduct();
                    }
                }
            }
        }
        return new PriceResponse(new BigDecimal(totalPrice), new BigDecimal(totalDiscount));
    }
    private Product getProduct(int id, String token){
        List<ServiceInstance> instances = discoveryClient.getInstances("PRODUCT_INFO");
        ServiceInstance serviceInstance = instances.get(0);
        String baseUrl = serviceInstance.getUri().toString();

        baseUrl = baseUrl + "/user/product/" + id;
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<?> requestBody = new HttpEntity<>( headers);
        return restTemplate.exchange(baseUrl, HttpMethod.GET, requestBody, Product.class).getBody();
    }
    private List<Product> listProductBasket(List<Basket> basketList, String token){
        List<Integer> productId = new ArrayList<>();
        List<Product> productList = new ArrayList<>();
        for(Basket b : basketList){
            productId.add(b.getProductId());
        }
        for(Integer id : productId){
            productList.add(getProduct(id, token));
        }
        return productList;
    }
    public List<Basket> getProduct(ProductRequest productRequest) {
        if (productRequest.getOrderId() == null) {
            return basketListNoOrder(productRequest.getUserId());
        } else {
            return basketListWhithOrder(productRequest.getUserId(), productRequest.getOrderId());
        }

    }

    /*
    Каждые 6 часов проверяет как долго находится последний добавленый товар в корзине,
    если с последнего добавления в корзину прошло 14 дней, то очишает корзину этого пользователя
     */
     @Scheduled(cron = "0 0 0/6 * * ?")
    //@Scheduled(fixedRate = 3000)
    public void checkBasket() {
        List<Basket> allBasketList = basketRepository.findAll();

        HashSet<Integer> users = new HashSet();
        allBasketList.stream().forEach(basket -> {
            users.add(basket.getUserId());
        });

        for (Integer userId : users) {
            List<Basket> basketListNoOrder = basketListNoOrder(userId);
            if (!basketListNoOrder.isEmpty()) {
                basketListNoOrder.sort(new Comparator<Basket>() {
                    public int compare(Basket b1, Basket b2) {
                        return b1.getDate().compareTo(b2.getDate());
                    }
                });

                Date date = basketListNoOrder.get(basketListNoOrder.size() - 1).getDate();
                Date currentDate = new Date();
                long milliseconds = currentDate.getTime() - date.getTime();
                int days = (int) (milliseconds / (24 * 60 * 60 * 1000));
                if (days == 14) {
                    basketListNoOrder.stream().forEach(basket -> {
                        deleteProduct(basket);
                    });
                }
            }

        }

    }


    private List<Basket> basketListNoOrder(Integer userId) {
        List<Basket> basketList = basketRepository.findAllByUserId(userId);
        List<Basket> basketListNoOrder = new ArrayList<>();
        for (Basket b : basketList) {
            if (b.getUserOrder() == null) {
                basketListNoOrder.add(b);
            }
        }
        return basketListNoOrder;
    }

    private List<Basket> basketListWhithOrder(Integer userId, Integer orderId) {
        List<Basket> basketList = basketRepository.findAllByUserId(userId);
        List<Basket> basketListWhithOrder = new ArrayList<>();
        for (Basket b : basketList) {
            if (b.getUserOrder() != null) {
                if (b.getUserOrder().equals(orderRepository.findAllById(orderId))) {
                    basketListWhithOrder.add(b);
                }

            }
        }
        return basketListWhithOrder;
    }
    public boolean checkProductPay(Integer userId, Integer orderId, String token){
         List<Basket> basketListWhithOrder = basketListWhithOrder(userId,orderId);
         for(Basket basket : basketListWhithOrder){
             if(getProduct(basket.getProductId(),token) == null){
                 return false;
             }
         }
         return true;
    }
}
