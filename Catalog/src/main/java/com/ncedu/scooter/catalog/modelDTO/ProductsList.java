package com.ncedu.scooter.catalog.modelDTO;

import java.util.ArrayList;
import java.util.List;

public class ProductsList {
    private List products;

    public ProductsList() {
        this.products = new ArrayList();
    }

    public ProductsList(List products) {
        this.products = products;
    }

    public List getProducts() {
        return products;
    }

    public void setProducts(List products) {
        this.products = products;
    }
}
