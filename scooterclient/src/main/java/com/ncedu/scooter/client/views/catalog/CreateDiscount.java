package com.ncedu.scooter.client.views.catalog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ncedu.scooter.client.model.product.Discount;
import com.ncedu.scooter.client.model.product.DiscountType;
import com.ncedu.scooter.client.model.product.Product;
import com.ncedu.scooter.client.service.ProductServiceAdmin;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
@AllArgsConstructor
public class CreateDiscount {
    private ProductServiceAdmin productService;
    private String token;

    public Component createDiscount(Grid<Product> productGrid, Grid<Discount> discountGrid, ListDataProvider<Product> dataProvider) {
        H3 h3 = new H3("New discount:");
        h3.setSizeFull();
        Dialog dialog = new Dialog();
        dialog.setWidth("600px");
        Discount discount = new Discount();

        VerticalLayout verticalLayout1 = new VerticalLayout();
        verticalLayout1.setAlignItems(FlexComponent.Alignment.CENTER);
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        HorizontalLayout horizontalLayout1 = new HorizontalLayout();
        horizontalLayout1.setAlignItems(FlexComponent.Alignment.CENTER);

        TextField name = new TextField("Discount name");
        name.setWidth("550px");
        TextField description = new TextField("Description");
        description.setWidth("550px");
        IntegerField value = new IntegerField("Value");

        DiscountType[] discountTypes = DiscountType.values();
        ComboBox<DiscountType> discountType = new ComboBox<>("Discount type");
        discountType.setItems(discountTypes);
        discountType.addValueChangeListener(discountTypeClick -> {
            discount.setDiscountType(discountTypeClick.getValue());
        });

        verticalLayout1.add(h3, name, description);
        horizontalLayout.add(value, discountType);

        Button close = new Button("Cancel", e -> {
            dialog.close();
        });
        Button save = new Button("Save", e -> {
            discount.setName(name.getValue());
            discount.setDescription(description.getValue());
            BigDecimal newValue = new BigDecimal(value.getValue());
            discount.setValue(newValue);
            discount.setDiscountType(discountType.getValue());
            try {
                boolean b = productService.saveDisconut(discount, token);
                if (b) {
                    ArrayList<Discount> discountNewList = productService.getDiscountList(token);
                    discountGrid.setItems(discountNewList);
                    //createDataProvider(productService);
                    productGrid.setDataProvider(dataProvider);
                    dialog.close();
                } else {
                    Notification.show("Try again!", 1500, Notification.Position.MIDDLE);
                }
            } catch (JsonProcessingException ex) {
                ex.getMessage();
            }

        });

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(close, save);
        dialog.add(verticalLayout1, horizontalLayout, buttonLayout);

        dialog.open();
        return dialog;
    }
    private Notification notification(String message, int time) {
        return Notification.show(message, time, Notification.Position.MIDDLE);
    }
}
