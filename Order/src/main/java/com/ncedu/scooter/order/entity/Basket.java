package com.ncedu.scooter.order.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Schema(description = "Entity User Basket")
@Entity(name = "basket")
@Table(name = "basket")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "userId", "userOrder", "productId", "countProduct", "date"})
public class Basket {
    public Basket(Basket basket) {
        this.id = basket.getId();
        this.userId = basket.getUserId();
        this.userOrder = basket.getUserOrder();
        this.productId = basket.getProductId();
        this.price = basket.getPrice();
        this.countProduct = basket.getCountProduct();
        this.date = basket.getDate();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Id")
    private Integer id;

    @Column(name = "user_id")
    @NotNull
    private Integer userId;
    @ManyToOne
    @JoinColumn(name = "user_order_id")
    private UserOrder userOrder;
    @Column(name = "product_id")
    private Integer productId;
    @Column(name = "product_price")
    private BigDecimal price;
    @Column(name = "count_product")
    private Integer countProduct;
    @Column(name = "date")
    private Date date;
}
