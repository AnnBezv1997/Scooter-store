package com.ncedu.scooter.client.views.catalog;

import com.ncedu.scooter.client.model.order.Basket;
import com.ncedu.scooter.client.model.product.Product;
import com.ncedu.scooter.client.model.user.User;
import com.ncedu.scooter.client.service.OrderService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

import static com.ncedu.scooter.client.views.catalog.Message.MESSAGE;

@AllArgsConstructor
public class PageProduct {
    private OrderService orderService;
    private Product showProduct;
    private User user;
    private String token;

    public Component pageProduct(){
        Dialog dialog = new Dialog();
        dialog.setWidth("600px");

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        Image logo = new Image("images/" + showProduct.getImage(), "ScooterClient logo");
        logo.setHeight("380px");
        logo.setWidth("380px");
        verticalLayout.add(logo);
        Span name = new Span();
        Span description = new Span();
        Span category = new Span();
        Span descriptionCategory = new Span();
        Span price = new Span();
        Span oldPrice = new Span();
        Span stockStatus = new Span();

        name.setText("Name : " + showProduct.getName());
        description.setText("Description : " + showProduct.getDescription());
        category.setText("Category : " + showProduct.getCategory().getName());
        descriptionCategory.setText(showProduct.getCategory().getDescription());
        BigDecimal p;
        if (showProduct.getDiscount() != null) {
            if (showProduct.getDiscount().getDiscountType().toString().equals("ABSOLUTE")) {
                double totalDiscount = showProduct.getDiscount().getValue().doubleValue();
                p = showProduct.getPrice().subtract(new BigDecimal(totalDiscount));
                price.setText(MESSAGE.get("New price") + p + " $.");
                oldPrice.setText(MESSAGE.get("Old price") + showProduct.getPrice() + " $.");


            } else {
                double totalDiscount = showProduct.getPrice().multiply(new BigDecimal(showProduct.getDiscount().getValue().doubleValue() / 100)).doubleValue();
                p = showProduct.getPrice().subtract(new BigDecimal(totalDiscount));
                price.setText(MESSAGE.get("New price") + p + " $.");
                oldPrice.setText(MESSAGE.get("Old price") + showProduct.getPrice() + " $.");
            }

        } else {
            p = showProduct.getPrice();
            price.setText(MESSAGE.get("Price") + p.doubleValue() + " $.");
        }

        if (showProduct.getStockStatus().getCount() > 0) {
            stockStatus.setText(MESSAGE.get("In stock"));
        } else {
            stockStatus.setText(MESSAGE.get("Out of stock"));
        }
        verticalLayout.add(name, description, category, descriptionCategory, price, oldPrice, stockStatus);
        Button close = new Button(MESSAGE.get("Cancel"), e -> {
            dialog.close();
        });
        if (!user.getRole().getName().equals("ROLE_ADMIN")) {
            NumberField countProduct = new NumberField();
            countProduct.setValue(1d);
            countProduct.setHasControls(true);
            countProduct.setMin(1);
            countProduct.setMax(showProduct.getStockStatus().getCount());

            Button addToBasket = new Button(MESSAGE.get("Add"), e -> {
                Basket basket = new Basket();
                basket.setUserId(user.getId());
                basket.setUserOrder(null);
                basket.setProductId(showProduct.getId());
                basket.setPrice(new BigDecimal(p.doubleValue()));
                basket.setCountProduct(countProduct.getValue().intValue());
                basket.setDate(new Date());
                boolean add = orderService.addProductBasket(basket, token);
                if (add) {
                    notification(MESSAGE.get("AddDone"), 1500);
                }
            });
            verticalLayout.add(new Div(addToBasket, countProduct));
        }

        dialog.add(verticalLayout);

        dialog.add(new Div(close));
        dialog.open();
        return dialog;
    }
    private Notification notification(String message, int time) {
        return Notification.show(message, time, Notification.Position.MIDDLE);
    }
}
