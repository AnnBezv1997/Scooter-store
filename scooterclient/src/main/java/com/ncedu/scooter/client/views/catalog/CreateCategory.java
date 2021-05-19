package com.ncedu.scooter.client.views.catalog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ncedu.scooter.client.model.product.Category;
import com.ncedu.scooter.client.model.product.Discount;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import lombok.AllArgsConstructor;

import java.util.ArrayList;

import static com.ncedu.scooter.client.views.catalog.Message.MESSAGE;

@AllArgsConstructor
public class CreateCategory {

    private ProductServiceAdmin productService;
    private String token;

    public Component createCategory(Grid<Product> productGrid, Grid<Category> categoryGrid, ListDataProvider<Product> dataProvider) throws JsonProcessingException {
        H3 h3 = new H3("New category:");
        h3.setSizeFull();
        Dialog dialog = new Dialog();
        dialog.setWidth("600px");
        Category category = new Category();

        VerticalLayout verticalLayout1 = new VerticalLayout();
        verticalLayout1.setAlignItems(FlexComponent.Alignment.CENTER);
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        HorizontalLayout horizontalLayout1 = new HorizontalLayout();
        horizontalLayout1.setAlignItems(FlexComponent.Alignment.CENTER);

        TextField name = new TextField("Category name");
        name.setWidth("550px");
        TextField description = new TextField("Description");
        description.setWidth("550px");

        ArrayList<Category> categoryArrayList = productService.getCategories(token);
        ComboBox<Category> categoryComboBox = new ComboBox<>("Category parent");
        categoryComboBox.setItemLabelGenerator(Category::getName);
        categoryComboBox.setItems(categoryArrayList);
        categoryComboBox.addValueChangeListener(discountTypeClick -> {
            category.setCategoryParent(discountTypeClick.getValue());
        });

        verticalLayout1.add(h3, name, description, categoryComboBox);


        Button close = new Button("Cancel", e -> {
            dialog.close();
        });
        Button save = new Button("Save", e -> {
            category.setName(name.getValue());
            category.setDescription(description.getValue());
            category.setCategoryParent(categoryComboBox.getValue());
            try {
                boolean b = productService.saveCategory(category, token);
                if (b) {
                    ArrayList<Category> categoryNewList = productService.getCategories(token);
                    categoryGrid.setItems(categoryNewList);
                    productGrid.setDataProvider(dataProvider);
                    dialog.close();
                } else {
                    notification(MESSAGE.get("Try"), 1500);
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
