package com.ncedu.scooter.client.views.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ncedu.scooter.client.model.order.Basket;
import com.ncedu.scooter.client.model.order.OrderStatusPay;
import com.ncedu.scooter.client.model.order.UserOrder;
import com.ncedu.scooter.client.model.product.Product;
import com.ncedu.scooter.client.model.request.order.ProductRequest;
import com.ncedu.scooter.client.model.request.user.AuthResponse;
import com.ncedu.scooter.client.model.user.Address;
import com.ncedu.scooter.client.model.user.User;
import com.ncedu.scooter.client.service.OrderService;
import com.ncedu.scooter.client.service.UserService;
import com.ncedu.scooter.client.views.catalog.ErrorView;
import com.ncedu.scooter.client.views.main.ViewCatalog;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

@Route(value = "basket", layout = ViewCatalog.class)
@PageTitle("Basket")
@CssImport("./views/order/basket-view.css")
public class BasketView extends Div {
    private AuthResponse authResponse = (AuthResponse) VaadinSession.getCurrent().getAttribute("authResponse");
    private String token = (String) VaadinSession.getCurrent().getAttribute("token");
    private User user;
    private Grid<Basket> productGrid = new Grid<>(Basket.class);
    private ListDataProvider<Basket> dataProvider;

    private Button totalPrice = new Button();

    private Grid.Column<Basket> product;
    private Grid.Column<Basket> count;
    private Grid.Column<Basket> price;
    private Grid.Column<Basket> delete;

    public BasketView(OrderService orderService, UserService userService) throws JsonProcessingException {
        if (authResponse == null) {
            VerticalLayout verticalLayout = new VerticalLayout();
            verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
            Image logo = new Image("images/error.png" , "ScooterClient error");
            logo.setHeight("410px");
            logo.setWidth("800px");
            verticalLayout.add(logo);
            add(verticalLayout);
            UI.getCurrent().navigate(ErrorView.class);

        }else if (authResponse.getUser().getRole().getName().equals("ROLE_ADMIN")) {
            VerticalLayout verticalLayout = new VerticalLayout();
            verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
            Image logo = new Image("images/error.png", "ScooterClient error");
            logo.setHeight("410px");
            logo.setWidth("800px");
            verticalLayout.add(logo);
            add(verticalLayout);
            UI.getCurrent().navigate(ErrorView.class);
        } else  {
            addClassName("basket-view");
            user = authResponse.getUser();
            createTitle();
            createDataProvider(orderService);
            add(createGrid());
            createProductColum(orderService);
            createCountColumn(orderService);
            createPriceColumn(orderService);
            createDeleteColumn(orderService);
            totalPrice(orderService);
            totalPrice.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            totalPrice.addClickListener(event -> {
                try {
                    createOrder(orderService, userService);

                } catch (JsonProcessingException ex) {
                    ex.getMessage();
                }
            });
            add(totalPrice);
        }

    }

    private Component createTitle() {
        H3 h3 = new H3("Basket");
        h3.setSizeFull();
        return h3;

    }

    private void createDataProvider(OrderService orderService) throws JsonProcessingException {
        ArrayList<Basket> basketArrayList = orderService.getProductBasket(new ProductRequest(null, user.getId()), token);
        basketArrayList.sort(new Comparator<Basket>() {
            public int compare(Basket b1, Basket b2) {
                return b1.getDate().compareTo(b2.getDate());
            }
        });
        dataProvider = new ListDataProvider<>(basketArrayList);
    }


    private void totalPrice(OrderService orderService) {
        totalPrice.setText("Total discount: " + orderService.totalPrice(user.getId(), token).getTotalDiscount() + " $. " +
                "Total price: " + orderService.totalPrice(user.getId(), token).getTotalPrice() + " $. Make an order");
    }

    private Component createGrid() {
        productGrid.setHeight("82%");
        productGrid.setDataProvider(dataProvider);
        productGrid.removeAllColumns();
        return productGrid;
    }

    private void createProductColum(OrderService orderService) {
        product = productGrid.addColumn(new ComponentRenderer<>(basket -> {
            VerticalLayout hl = new VerticalLayout();
            hl.setAlignItems(FlexComponent.Alignment.AUTO);
            Product product = orderService.getProduct(basket.getProductId(), token);
            if(product != null){
                Image logo = new Image("images/" + product.getImage(), "ScooterClient logo");
                Span span = new Span();
                span.setClassName("name");
                span.setText(product.getName());
                hl.add(logo, span);
                return hl;
            }else {
                Image logo = new Image("images/logo.png", "ScooterClient logo");
                hl.add(logo);
                return hl;
            }

        })).setHeader("Product");
    }

    private void createCountColumn(OrderService orderService) {
        count = productGrid.addColumn(new ComponentRenderer<>(basket -> {
            VerticalLayout hl = new VerticalLayout();
            hl.setAlignItems(FlexComponent.Alignment.AUTO);

            Product product = orderService.getProduct(basket.getProductId(), token);
            if(product != null){
                IntegerField countProduct = new IntegerField();
                countProduct.setValue(basket.getCountProduct());
                countProduct.setHasControls(true);
                countProduct.setMin(1);
                countProduct.setMax(product.getStockStatus().getCount());

                countProduct.addValueChangeListener(numberField -> {
                    basket.setCountProduct(numberField.getValue());
                    try {
                        boolean b1 = orderService.updateCountProductBasket(basket, token);
                        if (b1) {
                            createDataProvider(orderService);
                            totalPrice.setText("Total discount: " + orderService.totalPrice(user.getId(), token).getTotalDiscount() + " $. " +
                                    "Total price: " + orderService.totalPrice(user.getId(), token).getTotalPrice() + " $. Make an order");
                            productGrid.setDataProvider(dataProvider);

                        }
                    } catch (JsonProcessingException ex) {
                        ex.getMessage();
                    }

                });
                Span span = new Span();
                span.setClassName("count");
                span.setText("Count: " + basket.getCountProduct() + " pcs.");
                hl.add(span, countProduct);
                return hl;
            }else {
                Span span = new Span();
                span.setClassName("count");
                span.setText("The product is sold out");
                hl.add(span);
                return hl;
            }

        })).setHeader("Count");
    }

    private void createPriceColumn(OrderService orderService) {
        price = productGrid.addColumn(new ComponentRenderer<>(basket -> {
            VerticalLayout hl = new VerticalLayout();
            hl.setAlignItems(FlexComponent.Alignment.AUTO);
            Product product = orderService.getProduct(basket.getProductId(), token);
            if(product != null){
                Span span = new Span();
                span.setClassName("price");

                span.setText("Price: " + basket.getPrice().multiply(new BigDecimal(basket.getCountProduct()).setScale(1,BigDecimal.ROUND_HALF_UP)) + " $.");
                hl.add(span);
                return hl;
            }else {
                Span span = new Span();
                span.setClassName("count");
                span.setText("");
                hl.add(span);
                return hl;
            }

        })).setHeader("Price");
    }

    private void createDeleteColumn(OrderService orderService) {
        delete = productGrid.addColumn(new ComponentRenderer<>(basket -> {
            VerticalLayout hl = new VerticalLayout();
            hl.setAlignItems(FlexComponent.Alignment.AUTO);

                Button button = new Button("Delete", deleteEvent -> {

                    try {
                        boolean b1 = orderService.deleteProductBasket(basket, token);
                        if (b1) {
                            createDataProvider(orderService);
                            totalPrice.setText("Total discount: " + orderService.totalPrice(user.getId(), token).getTotalDiscount() + " $. " +
                                    "Total price: " + orderService.totalPrice(user.getId(), token).getTotalPrice() + " $. Make an order");
                            productGrid.setDataProvider(dataProvider);
                        }
                    } catch (JsonProcessingException ex) {
                        ex.getMessage();
                    }


                });
                hl.add(button);
                return hl;

        })).setHeader("Delete");
    }

    private void createOrder(OrderService orderService, UserService userService) throws JsonProcessingException {
        Dialog dialog = new Dialog();
        dialog.setWidth("400px");
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

        ArrayList<Address> addressResponse = userService.getAllAddress(user.getLogin(), token);
        H3 h3 = new H3("Select an address");
        verticalLayout.add(h3);
        UserOrder userOrder = new UserOrder();
        ComboBox<Address> addressComboBox = new ComboBox<>();
        addressComboBox.setItemLabelGenerator(Address::getAddress);
        addressComboBox.setItems(addressResponse);
        addressComboBox.setPlaceholder("Select an address");
        addressComboBox.setClearButtonVisible(true);
        addressComboBox.setWidth("100%");
        addressComboBox.addValueChangeListener(
                event -> {
                    if (event.getValue() != null) {
                        userOrder.setUserId(user.getId());
                        userOrder.setAddress(event.getValue().getAddress());
                        userOrder.setDate(new Date());
                        userOrder.setTotalPrice(orderService.totalPrice(user.getId(), token).getTotalPrice());
                        userOrder.setOrderStatusPay(OrderStatusPay.NOT);
                        userOrder.setOrderStatus(null);

                    } else {
                        userOrder.setUserId(user.getId());
                        userOrder.setAddress(null);
                        userOrder.setDate(new Date());
                        userOrder.setTotalPrice(orderService.totalPrice(user.getId(), token).getTotalPrice());
                        userOrder.setOrderStatusPay(OrderStatusPay.NOT);
                        userOrder.setOrderStatus(null);

                    }

                });
        Button newAddress = new Button("New address");
        newAddress.addClickListener( event -> {
            newAddress.getUI().ifPresent(ui -> {
                ui.navigate("address");
                dialog.close();
            });
        });
        if(addressComboBox.isEmpty()){
            verticalLayout.add(newAddress);
        }
        verticalLayout.add(addressComboBox);
        Button close = new Button("Cancel", e -> {
            dialog.close();
        });
        Button next = new Button("Next ->", e -> {
            Dialog dialogPay = new Dialog();
            dialogPay.setWidth("400px");
            VerticalLayout verticalLayout1 = new VerticalLayout();
            verticalLayout1.setAlignItems(FlexComponent.Alignment.STRETCH);

            TextField cardNumber = new TextField("Credit card number");
            cardNumber.setPlaceholder("1234 5678 9123 4567");
            cardNumber.setPattern("[\\d ]*");
            cardNumber.setPreventInvalidInput(true);
            cardNumber.setRequired(true);
            cardNumber.setErrorMessage("Please enter a valid credit card number");

            TextField code = new TextField("CVC/CVV code");
            code.setPlaceholder("123");
            code.setPattern("[\\d ]*");
            code.setPreventInvalidInput(true);
            code.setRequired(true);
            code.setErrorMessage("Please enter a valid CVC/CVV code");

            Button closepay = new Button("Cancel", e1 -> {
                userOrder.setOrderStatusPay(OrderStatusPay.NOT);
                boolean b = orderService.createOrder(userOrder, token);
                Notification.show("Go to the orders section!", 2000, Notification.Position.MIDDLE);
                dialogPay.close();
                dialog.close();
            });
            Button pay = new Button("Pay", e1 -> {
                String card = cardNumber.getValue().replaceAll(" ", "");
                String cod = code.getValue().replaceAll(" ", "");
                if (card.length() == 16 && cod.length() == 3) {
                    userOrder.setOrderStatusPay(OrderStatusPay.YES);
                    boolean b = orderService.createOrder(userOrder, token);
                    if (b) {
                        dialogPay.close();
                        dialog.close();
                        try {
                            createDataProvider(orderService);
                            productGrid.setDataProvider(dataProvider);
                        }catch (JsonProcessingException ex){
                            ex.getMessage();
                        }

                        Notification.show("Done!Go to the orders section!", 2000, Notification.Position.MIDDLE);
                    }else{
                        try {
                            createDataProvider(orderService);
                            productGrid.setDataProvider(dataProvider);
                        }catch (JsonProcessingException ex){
                            ex.getMessage();
                        }
                        Notification.show("Some of your products have been deleted! Check your basket!", 2000, Notification.Position.MIDDLE);
                        dialogPay.close();
                        dialog.close();
                    }

                } else {
                    Notification.show("Please, check your credit card details!", 2000, Notification.Position.MIDDLE);
                }
            });
            pay.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            verticalLayout1.add(cardNumber, code);
            dialogPay.add(verticalLayout1);
            dialogPay.add(new Div(closepay, pay));
            dialogPay.open();


        });
        next.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        dialog.add(verticalLayout);
        dialog.add(new Div(close, next));
        dialog.open();

    }


}
