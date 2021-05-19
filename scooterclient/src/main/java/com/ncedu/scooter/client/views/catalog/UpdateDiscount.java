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

import static com.ncedu.scooter.client.views.catalog.Message.MESSAGE;

@AllArgsConstructor
public class UpdateDiscount {
    private Discount discountItem;
    private ProductServiceAdmin productService;
    private Dialog dialog;
    private String token;

    public Component updateDiscount(Grid<Product> productGrid, Grid<Discount> discountGrid, ListDataProvider<Product> dataProvider) {
        H3 h3 = new H3("Update discount:");
        h3.setSizeFull();
        dialog.setWidth("600px");

        VerticalLayout verticalLayout1 = new VerticalLayout();
        verticalLayout1.setAlignItems(FlexComponent.Alignment.CENTER);
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        TextField name = new TextField("Discount name");
        name.setWidth("550px");
        name.setValue(discountItem.getName());
        TextField description = new TextField("Description");
        description.setWidth("550px");
        description.setValue(discountItem.getDescription());
        IntegerField value = new IntegerField("Value");
        value.setValue(discountItem.getValue().intValue());

        DiscountType[] discountTypes = DiscountType.values();
        ComboBox<DiscountType> discountType = new ComboBox<>("Discount type");
        discountType.setItems(discountTypes);
        discountType.setValue(discountItem.getDiscountType());
        discountType.addValueChangeListener(discountTypeClick -> {
            discountItem.setDiscountType(discountTypeClick.getValue());
        });

        verticalLayout1.add(h3, name, description);
        horizontalLayout.add(value, discountType);

        Button close = new Button("Cancel", e -> {
            dialog.close();
        });
        Button save = new Button("Save", e -> {
            discountItem.setName(name.getValue());
            discountItem.setDescription(description.getValue());
            BigDecimal newValue = new BigDecimal(value.getValue());
            discountItem.setValue(newValue);
            discountItem.setDiscountType(discountType.getValue());
            try {
                boolean b = productService.updateDiscount(discountItem, token);
                if (b) {
                    updateGrid(discountGrid,productGrid,dataProvider);
                } else {
                    notification(MESSAGE.get("Try"), 1500);
                }
            } catch (JsonProcessingException ex) {
                ex.getMessage();
            }
        });
        Button delete = new Button("Delete", e -> {
            try {
                boolean b = productService.deleteDiscount(discountItem, token);
                if (b) {
                    updateGrid(discountGrid,productGrid,dataProvider);
                } else {
                    notification(MESSAGE.get("Try"), 1500);
                }
            } catch (JsonProcessingException ex) {
                ex.getMessage();
            }


        });

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(close, save, delete);
        dialog.add(verticalLayout1, horizontalLayout, buttonLayout);
        return dialog;
    }
    private Notification notification(String message, int time) {
        return Notification.show(message, time, Notification.Position.MIDDLE);
    }
    private void updateGrid(Grid<Discount> discountGrid, Grid<Product> productGrid, ListDataProvider<Product> dataProvider)throws JsonProcessingException{
        ArrayList<Discount> discountNewList = productService.getDiscountList(token);
        discountGrid.setItems(discountNewList);
        productGrid.setDataProvider(dataProvider);
        dialog.close();
    }
}
