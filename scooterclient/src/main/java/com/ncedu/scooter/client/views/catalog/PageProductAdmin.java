package com.ncedu.scooter.client.views.catalog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ncedu.scooter.client.model.product.Category;
import com.ncedu.scooter.client.model.product.Discount;
import com.ncedu.scooter.client.model.product.Product;
import com.ncedu.scooter.client.model.product.StockStatus;
import com.ncedu.scooter.client.model.user.User;
import com.ncedu.scooter.client.service.OrderService;
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
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import static com.ncedu.scooter.client.views.catalog.Message.MESSAGE;
@AllArgsConstructor
public class PageProductAdmin {
    private ProductServiceAdmin productService;
    private Product showProduct;
    private String token;

    public PageProductAdmin(ProductServiceAdmin productService, String token) {
        this.productService = productService;
        this.token = token;
    }

    public Component pageProduct(Grid<Product> productGrid, ListDataProvider<Product> dataProvider) throws JsonProcessingException {
        H3 h3 = new H3("Update product:");
        h3.setSizeFull();
        Dialog dialog = new Dialog();
        dialog.setWidth("600px");

        VerticalLayout verticalLayout1 = new VerticalLayout();
        verticalLayout1.setAlignItems(FlexComponent.Alignment.CENTER);
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        HorizontalLayout horizontalLayout1 = new HorizontalLayout();
        horizontalLayout1.setAlignItems(FlexComponent.Alignment.CENTER);


        TextField name = new TextField("Product name");
        name.setValue(showProduct.getName());
        name.setWidth("550px");
        TextField description = new TextField("Description");
        description.setValue(showProduct.getDescription());
        description.setWidth("550px");
        NumberField price = new NumberField("Price");
        price.setValue(showProduct.getPrice().doubleValue());


        TextField image = new TextField("Image");
        image.setValue(showProduct.getImage());


        IntegerField stockStatus = new IntegerField("Stock Status");
        stockStatus.setValue(showProduct.getStockStatus().getCount());


        ArrayList<Category> categoryArrayList = productService.getCategories(token);
        ComboBox<Category> category = new ComboBox<>("Category");
        category.setItemLabelGenerator(Category::getName);
        category.setItems(categoryArrayList);
        category.setValue(showProduct.getCategory());
        category.addValueChangeListener(categoryClick -> {
            showProduct.setCategory(categoryClick.getValue());
        });

        ArrayList<Discount> discountArrayList = productService.getDiscountList(token);
        ComboBox<Discount> discount = new ComboBox<>("Discount");
        discount.setItemLabelGenerator(Discount::getName);
        discount.setItems(discountArrayList);
        discount.setValue(showProduct.getDiscount());
        discount.addValueChangeListener(discountClick -> {
            showProduct.setDiscount(discountClick.getValue());
        });

        verticalLayout1.add(h3, name, description);
        horizontalLayout.add(price, image, stockStatus);
        horizontalLayout1.add(category, discount);

        Button close = new Button("Cancel", e -> {
            dialog.close();
        });
        Button save = new Button("Save", e -> {

            showProduct.setName(name.getValue());
            showProduct.setDescription(description.getValue());
            BigDecimal newPrice = new BigDecimal(price.getValue());
            showProduct.setPrice(newPrice);
            showProduct.setImage(image.getValue());
            showProduct.setCategory(category.getValue());
            showProduct.setDiscount(discount.getValue());
            StockStatus newStockStatus = new StockStatus(showProduct.getStockStatus().getId(), stockStatus.getValue());

            boolean st = productService.updateStockStatus(newStockStatus, token);
            if (st) {
                showProduct.setStockStatus(newStockStatus);
            }

            boolean b = productService.updateProduct(showProduct, token);
            if (b) {
               // createDataProvider(productService);
                productGrid.setDataProvider(dataProvider);
            } else {
                notification(MESSAGE.get("Try"), 1500);
            }
            dialog.close();
        });
        Button detele = new Button("Delete", e -> {
            boolean b = productService.deleteProduct(showProduct, token);
            if (b) {
               // createDataProvider(productService);
                productGrid.setDataProvider(dataProvider);
            } else {
                notification(MESSAGE.get("Try"), 1500);
            }
            dialog.close();
        });
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(close, save, detele);
        dialog.add(verticalLayout1, horizontalLayout, horizontalLayout1, buttonLayout);

        dialog.open();
        return dialog;
    }
    public Component pageNewProduct(Grid<Product> productGrid, ListDataProvider<Product> dataProvider) throws JsonProcessingException {
        H3 h3 = new H3("New product:");
        h3.setSizeFull();

        Dialog dialog = new Dialog();
        dialog.setWidth("600px");
        Product newProduct = new Product();
        VerticalLayout verticalLayout1 = new VerticalLayout();
        verticalLayout1.setAlignItems(FlexComponent.Alignment.CENTER);
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        HorizontalLayout horizontalLayout1 = new HorizontalLayout();
        horizontalLayout1.setAlignItems(FlexComponent.Alignment.CENTER);


        TextField name = new TextField("Product name");
        name.setWidth("550px");
        TextField description = new TextField("Description");

        description.setWidth("550px");
        IntegerField price = new IntegerField("Price");

        TextField image = new TextField("Image");
        IntegerField stockStatus = new IntegerField("Stock Status");

        ArrayList<Category> categoryArrayList = productService.getCategories(token);
        ComboBox<Category> category = new ComboBox<>("Category");
        category.setItemLabelGenerator(Category::getName);
        category.setItems(categoryArrayList);

        ArrayList<Discount> discountArrayList = productService.getDiscountList(token);
        ComboBox<Discount> discount = new ComboBox<>("Discount");
        discount.setItemLabelGenerator(Discount::getName);
        discount.setItems(discountArrayList);

        verticalLayout1.add(h3, name, description);
        horizontalLayout.add(price, stockStatus, image);
        horizontalLayout1.add(category, discount);

        Button close = new Button("Cancel", e -> {
            dialog.close();
        });
        Button save = new Button("Save", e -> {
            newProduct.setName(name.getValue());
            newProduct.setDescription(description.getValue());
            BigDecimal newPrice = new BigDecimal(price.getValue());
            newProduct.setPrice(newPrice);
            newProduct.setImage(image.getValue());
            newProduct.setCategory(category.getValue());
            newProduct.setDiscount(discount.getValue());
            StockStatus newStockStatus = new StockStatus();
            newStockStatus.setCount(stockStatus.getValue());
            newProduct.setStockStatus(newStockStatus);

            boolean b = productService.saveProduct(newProduct, token);
            if (b) {

                productGrid.setDataProvider(dataProvider);
            } else {
                notification(MESSAGE.get("Try"), 1500);
            }
            dialog.close();
        });

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(close, save);
        dialog.add(verticalLayout1, horizontalLayout, horizontalLayout1, buttonLayout);

        dialog.open();
        return dialog;
    }
    private Notification notification(String message, int time) {
        return Notification.show(message, time, Notification.Position.MIDDLE);
    }
}
