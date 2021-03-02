package com.ncedu.scooter.product.info.entity;


import javax.persistence.*;

import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;

@Entity(name="product")
@Table(name="product")
@Indexed

public class Product {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO )
    @Column(name = "code")
    private int code;

    @Field
    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private double price;

    @Column(name = "category")

    private String category;

    @Column(name = "description")

    private String description;
    @Column(name = "manufacturer")

    private String manufacturer;


    public Product() {
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getManufacturer() {

        return manufacturer;
    }

    @Override
    public String toString() {
        return "Product{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
