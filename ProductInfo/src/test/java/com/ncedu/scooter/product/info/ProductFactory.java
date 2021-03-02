package com.ncedu.scooter.product.info;

import com.ncedu.scooter.product.info.entity.Product;

public class ProductFactory {
    private static final int PRODUCT_CODE = 10;
    private static final String PRODUCT_NAME = "product_name";
    private static final String PRODUCT_CATRGORY = "product_category";
    private static final double PRODUCT_PRISE = 100;
    private static final String PRODUCT_DESCRIPTION = "product_description";
    private static final String PRODUCT_MANUFACTURER = "product_manufacturer";

    public Product getProduct() {
        Product product = new Product();
        product.setCode(PRODUCT_CODE);
        product.setName(PRODUCT_NAME);
        product.setCategory(PRODUCT_CATRGORY);
        product.setPrice(PRODUCT_PRISE);
        product.setDescription(PRODUCT_DESCRIPTION);
        product.setManufacturer(PRODUCT_MANUFACTURER);
        return product;
    }
}
