package com.ncedu.scooter.order.entity;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.GenerationType;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Schema(description = "Entity User Order")
@Entity(name = "user_order")
@Table(name = "user_order")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "userId", "address", "date", "totalPrice", "orderStatusPay", "orderStatus"})
public class UserOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Id")
    private Integer id;

    @Column(name = "user_id")
    @NotNull
    private Integer userId;

    @Column(name = "address")
    @NotEmpty
    private String address;

    @Column(name = "date")
    private Date date;

    @Column(name = "total_price")
    private BigDecimal totalPrice;
    @Enumerated(EnumType.STRING)
    @Column(name = "status_pay")
    @Type(type = "com.ncedu.scooter.order.service.EnumTypePostgreSql")
    private OrderStatusPay orderStatusPay;
    @Enumerated(EnumType.STRING)
    @Column(name = "status_order")
    @Type(type = "com.ncedu.scooter.order.service.EnumTypePostgreSql")
    private OrderStatus orderStatus;

}
