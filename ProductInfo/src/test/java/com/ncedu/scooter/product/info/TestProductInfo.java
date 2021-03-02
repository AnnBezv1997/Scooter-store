package com.ncedu.scooter.product.info;

import com.ncedu.scooter.product.info.dao.ProductDAO;
import com.ncedu.scooter.product.info.entity.Product;
import com.ncedu.scooter.product.info.service.ProductService;
import com.ncedu.scooter.product.info.service.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
//@RunWith(JUnitPlatform.class)
public class TestProductInfo {
    @Mock
    private ProductDAO productDAO;

    private ProductService productService;
    private Product product;
    @BeforeEach
    void init(){
        product = new ProductFactory().getProduct();
        productService = new ProductServiceImpl(productDAO);
    }
    @Test
    public void getAllProducts(){
        when(productDAO.getAllProducts())
                .thenReturn(Collections.singletonList(product));

        List<Product> retrieved = productService.getAllProducts();
        assertThat(retrieved, contains(product));
    }
    @Test
    public void getProduct(){
        when(productDAO.getProduct(product.getCode()))
                .thenReturn(product);

        Product retrieved = productService.getProduct(product.getCode());
        assertThat(retrieved, equalTo(product));
    }
    @Test
    public void getProductsCategory(){
        when(productDAO.getProductsCategory(product.getCategory()))
                .thenReturn(Collections.singletonList(product));

        List<Product> retrieved = productService.getProductsCategory(product.getCategory());
        System.out.println(retrieved);
        assertThat(retrieved, contains(product));
    }


}
