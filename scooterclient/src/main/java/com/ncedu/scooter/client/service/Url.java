package com.ncedu.scooter.client.service;

import java.util.HashMap;

public final class Url {
    static final HashMap<String, String> URL = new HashMap<>();

    static {
        URL.put("AUTH_URL", "/gateway/auth");
        URL.put("REGISTER_URL", "/gateway/register");
        //Settings User
        URL.put("ADD_ADDRESS", "/gateway/user/address/add");
        URL.put("DELETE_ADDRESS", "/gateway/user/address/delete");
        URL.put("ADD_USER_NAME", "/gateway/user/name/add");
        URL.put("UPDATE_USER_LOGIN", "/gateway/user/login/update");
        URL.put("ALL_ADDRESS", "/gateway/user/address/");
        URL.put("USER", "/gateway/user/");
        //Product
        URL.put("PRODUCT_ID", "/product/user/product/");
        URL.put("CATEGORIES_PRODUCTS", "/product/user/categories");
        URL.put("PAGE_PRODUCTS", "/product/user/page/products");
        //Product admin
        URL.put("ADMIN_PAGE_PRODUCTS", "/product/admin/page/products");
        URL.put("ADMIN_CATEGORIES", "/product/admin/categories");
        URL.put("ADMIN_DISCOUNTS", "/product/admin/discount");

        URL.put("ADMIN_UPDATE_PRODUCT", "/product/admin/update/product");
        URL.put("ADMIN_UPDATE_CATEGORY", "/product/admin/update/category");
        URL.put("ADMIN_UPDATE_DISCOUNT", "/product/admin/update/discount");
        URL.put("ADMIN_UPDATE_STOCK_STATUS", "/product/admin/update/status");

        URL.put("ADMIN_DELETE_PRODUCT", "/product/admin/delete/product");
        URL.put("ADMIN_DELETE_CATEGORY", "/product/admin/delete/category");
        URL.put("ADMIN_DELETE_DISCOUNT", "/product/admin/delete/discount");

        URL.put("ADMIN_ADD_PRODUCT", "/product/admin/add/product");
        URL.put("ADMIN_ADD_CATEGORY", "/product/admin/add/category");
        URL.put("ADMIN_ADD_STOCK_STATUS", "/product/admin/add/status");
        URL.put("ADMIN_ADD_DISCOUNT", "/product/admin/add/discount");
        //Basket
        URL.put("PRODUCT_BASKET", "/order/basket/products");
        URL.put("ADD_PRODUCT_BASKET", "/order/add/product");
        URL.put("DELETE_PRODUCT_BASKET", "/order/delete/product");
        URL.put("UPDATE_COUNT_PRODUCT", "/order/update/count");
        URL.put("TOTAL_PRICE", "/order/total/price/");
        //Order
        URL.put("ORDERS", "/order/");
        URL.put("ORDER_CREATE", "/order/create");
        URL.put("ORDER_DELETE", "/order/delete");
        URL.put("ORDER_UPDATE", "/order/update");

    }
}
