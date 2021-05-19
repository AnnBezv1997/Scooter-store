package com.ncedu.scooter.client.views.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ncedu.scooter.client.model.order.Basket;
import com.ncedu.scooter.client.model.order.OrderStatus;
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
import com.ncedu.scooter.client.views.catalog.PageProduct;
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
import com.vaadin.flow.component.textfield.NumberField;
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

@Route(value = "order", layout = ViewCatalog.class)
@PageTitle("Order")
@CssImport("./views/order/order-view.css")
public class OrderView extends Div {
    private AuthResponse authResponse = (AuthResponse) VaadinSession.getCurrent().getAttribute("authResponse");
    private String token = (String) VaadinSession.getCurrent().getAttribute("token");
    private User user;
    private Grid<UserOrder> orderGrid = new Grid<>(UserOrder.class);
    private ListDataProvider<UserOrder> dataProvider;

    private Grid.Column<UserOrder> address;
    private Grid.Column<UserOrder> date;
    private Grid.Column<UserOrder> price;
    private Grid.Column<UserOrder> statusPay;
    private Grid.Column<UserOrder> status;

    public OrderView(OrderService orderService, UserService userService) throws JsonProcessingException {
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
            user = authResponse.getUser();
            addClassName("order-view");
            createTitle();
            createDataProvider(orderService);
            add(createGrid());
            createAddressColum(orderService, userService);
            createDateColum();
            createPriceColum();
            createStatusPayColum(orderService, userService);
            createStatusColum(orderService);
            orderGrid.addItemClickListener(event -> {
                if (event.getItem().getOrderStatus() == null || event.getItem().getOrderStatus().equals(OrderStatus.DELIVERED)) {
                    try {
                        basketOrder(orderService, event.getItem());
                    } catch (JsonProcessingException e) {
                        e.getMessage();
                    }
                }

            });
        }

    }

    private Component createTitle() {
        H3 h3 = new H3("Order");
        h3.setSizeFull();
        return h3;

    }

    private void createDataProvider(OrderService orderService) throws JsonProcessingException {
        ArrayList<UserOrder> userOrders = orderService.getUserOrder(user.getId(), token);
        userOrders.sort(new Comparator<UserOrder>() {
            public int compare(UserOrder b1, UserOrder b2) {
                return b1.getDate().compareTo(b2.getDate());
            }
        });
        dataProvider = new ListDataProvider<>(userOrders);
    }

    private Component createGrid() {
        orderGrid.setHeight("82%");
        orderGrid.setDataProvider(dataProvider);
        orderGrid.removeAllColumns();
        return orderGrid;
    }

    private void createAddressColum(OrderService orderService, UserService userService) {
        address = orderGrid.addColumn(new ComponentRenderer<>(userOrder -> {
            UserOrder order = userOrder;
            VerticalLayout hl = new VerticalLayout();
            hl.setAlignItems(FlexComponent.Alignment.START);
            Span address = new Span();
            address.setText("Delivery Address: " + order.getAddress());
            hl.add(address);
            if (order.getOrderStatus() == null) {
                ArrayList<Address> addressResponse = new ArrayList<>();
                try {
                    addressResponse = userService.getAllAddress(user.getLogin(), token);

                } catch (JsonProcessingException ex) {
                    ex.getMessage();
                }

                ComboBox<Address> addressComboBox = new ComboBox<>();
                addressComboBox.setItemLabelGenerator(Address::getAddress);
                addressComboBox.setItems(addressResponse);
                addressComboBox.setPlaceholder("You can change the address");
                addressComboBox.setClearButtonVisible(true);
                addressComboBox.setWidth("100%");
                addressComboBox.addValueChangeListener(
                        event -> {
                            if (event.getValue() != null) {
                                order.setAddress(event.getValue().getAddress());
                                boolean b = orderService.updateOrder(order, token);
                                if (b) {
                                    try {
                                        createDataProvider(orderService);
                                        orderGrid.setDataProvider(dataProvider);
                                        Notification.show("The delivery address has been changed!", 1500, Notification.Position.MIDDLE);

                                    } catch (JsonProcessingException ex) {
                                        ex.getMessage();
                                    }

                                }
                            }

                        });
                hl.add(addressComboBox);
            }

            return hl;
        })).setHeader("Address");
    }

    private void createDateColum() {
        date = orderGrid.addColumn(order -> order.getDate().toString()).setHeader("Date");

    }

    private void createPriceColum() {
        price = orderGrid.addColumn(order -> order.getTotalPrice().toString() + " $").setHeader("Price");

    }

    private void createStatusPayColum(OrderService orderService, UserService userService) {
        address = orderGrid.addColumn(new ComponentRenderer<>(userOrder -> {
            UserOrder order = userOrder;
            VerticalLayout hl = new VerticalLayout();
            hl.setAlignItems(FlexComponent.Alignment.START);
            Span span = new Span();
            hl.add(span);
            if (order.getOrderStatus() == null) {
                if (order.getOrderStatusPay().equals(OrderStatusPay.NOT)) {
                    span.setText("Payment status: not paid ");
                    Button pay = new Button("To pay");
                    pay.addClickListener(event -> {
                        payOrder(orderService, order);
                    });
                    pay.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                    hl.add(pay);
                } else {
                    span.setText("Payment status: paid for ");
                }
            }


            return hl;
        })).setHeader("Payment status");
    }

    private void payOrder(OrderService orderService, UserOrder userOrder) {
        Dialog dialogPay = new Dialog();
        dialogPay.setWidth("300px");
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

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

        Button close = new Button("Cancel", e -> {
            dialogPay.close();
        });
        Button pay = new Button("Pay", e -> {
            String card = cardNumber.getValue().replaceAll(" ", "");
            String cod = code.getValue().replaceAll(" ", "");
            if (card.length() == 16 && cod.length() == 3) {
                userOrder.setOrderStatusPay(OrderStatusPay.YES);
                boolean b = orderService.updateOrder(userOrder, token);
                if (b) {
                    try {
                        createDataProvider(orderService);
                        orderGrid.setDataProvider(dataProvider);
                        Notification.show("Done!", 1500, Notification.Position.MIDDLE);

                    } catch (JsonProcessingException ex) {
                        ex.getMessage();
                    }
                    dialogPay.close();

                }

            } else {
                Notification.show("Please, check your credit card details!", 1500, Notification.Position.MIDDLE);
            }
        });
        verticalLayout.add(cardNumber, code);
        dialogPay.add(verticalLayout);
        dialogPay.add(new Div(close, pay));
        dialogPay.open();
    }

    private void createStatusColum(OrderService orderService) {
        address = orderGrid.addColumn(new ComponentRenderer<>(userOrder -> {
            UserOrder order = userOrder;
            VerticalLayout hl = new VerticalLayout();
            hl.setAlignItems(FlexComponent.Alignment.START);
            Span span = new Span();
            hl.add(span);

            if (order.getOrderStatus() == null) {
                span.setText("Status: In progress");
                Button cancel = new Button("Cancel");
                cancel.addClickListener(event -> {
                    cancelOrder(orderService, userOrder);
                    span.setText("Status: Сanceled");
                });
                hl.add(cancel);
                cancel.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                if (order.getOrderStatusPay().equals(OrderStatusPay.YES)) {
                    Button deliv = new Button("Deliver");
                    deliv.addClickListener(event -> {
                        delivOrder(orderService, order);
                        span.setText("Status: Delivered");
                    });
                    deliv.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                    hl.add(deliv);
                }


            } else if (order.getOrderStatus().equals(OrderStatus.CANCELED)) {
                span.setText("Status: Сanceled");
            } else {
                span.setText("Status: Delivered");
            }

            return hl;
        })).setHeader("Status");
    }

    private void cancelOrder(OrderService orderService, UserOrder order) {

        try {
            boolean cancelBasket = orderService.deleteOrder(new ProductRequest(order.getId(), user.getId()), token);
            order.setOrderStatus(OrderStatus.CANCELED);
            boolean cancelOrder = orderService.updateOrder(order, token);

            if (cancelOrder && cancelBasket) {
                createDataProvider(orderService);
                orderGrid.setDataProvider(dataProvider);
                Notification.show("Done!", 1500, Notification.Position.MIDDLE);
            }

        } catch (JsonProcessingException ex) {
            ex.getMessage();
        }
    }

    private void delivOrder(OrderService orderService, UserOrder order) {
        try {
            order.setOrderStatus(OrderStatus.DELIVERED);
            boolean b = orderService.updateOrder(order, token);
            if (b) {
                createDataProvider(orderService);
                orderGrid.setDataProvider(dataProvider);
                Notification.show("Done!", 1500, Notification.Position.MIDDLE);
            }

        } catch (JsonProcessingException ex) {
            ex.getMessage();
        }
    }

    private ListDataProvider<Product> data;
    private Grid<Product> productGrid = new Grid<>(Product.class);
    private Grid.Column<Product> name;
    private Grid.Column<Product> description;

    private void basketProductOrder(OrderService orderService, UserOrder userOrder) throws JsonProcessingException {
        ArrayList<Basket> basketArrayList = orderService.getProductBasket(new ProductRequest(userOrder.getId(), user.getId()), token);
        ArrayList<Product> products = new ArrayList<>();
        basketArrayList.stream().forEach(basket -> {
            Product product = orderService.getProduct(basket.getProductId(), token);
            products.add(product);
        });

        data = new ListDataProvider<>(products);
    }

    private Component createGridProductOrder() {
        productGrid.setDataProvider(data);
        productGrid.removeAllColumns();
        return productGrid;
    }

    private void createNameProductColum(OrderService orderService) {
        name = productGrid.addColumn(new ComponentRenderer<>(product -> {
            VerticalLayout hl = new VerticalLayout();
            hl.setAlignItems(FlexComponent.Alignment.AUTO);
            Image logo = new Image("images/" + product.getImage(), "ScooterClient logo");
            logo.setWidth("30px");
            logo.setHeight("30px");
            Span span = new Span();
            span.setClassName("name");
            span.setText(product.getName());
            hl.add(logo, span);
            return hl;
        })).setHeader("Product");
    }

    private void createDescriptionProductColum(OrderService orderService) {
        productGrid.addColumn(new ComponentRenderer<>(product -> {
            VerticalLayout hl = new VerticalLayout();
            hl.setAlignItems(FlexComponent.Alignment.AUTO);
            Span span = new Span();
            span.setClassName("descriprion");
            span.setText(product.getDescription());
            hl.add(span);
            return hl;
        })).setHeader("Desctiption");
    }

    private void basketOrder(OrderService orderService, UserOrder userOrder) throws JsonProcessingException {
        Dialog dialog = new Dialog();
        dialog.setWidth("700px");
        basketProductOrder(orderService, userOrder);
        dialog.add(createGridProductOrder());
        createNameProductColum(orderService);
        createDescriptionProductColum(orderService);

        productGrid.addItemClickListener(basketItemClickEvent -> {

            pageProduct(basketItemClickEvent.getItem(), orderService);
        });
        Button close = new Button("Cancel", e -> {
            dialog.close();
        });
        dialog.add(new Div(close));
        dialog.open();
    }

    private void pageProduct(Product showProduct, OrderService orderService) {
        PageProduct pageProduct = new PageProduct(orderService,showProduct,user,token);
        pageProduct.pageProduct();
    }
}
