package com.ncedu.scooter.client.views.address;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ncedu.scooter.client.model.request.user.AuthResponse;
import com.ncedu.scooter.client.model.user.Address;
import com.ncedu.scooter.client.model.user.User;
import com.ncedu.scooter.client.service.UserService;
import com.ncedu.scooter.client.views.catalog.ErrorView;
import com.ncedu.scooter.client.views.main.ViewCatalog;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.sql.SQLOutput;
import java.util.ArrayList;

import static com.ncedu.scooter.client.views.address.Message.MESSAGE;


@Route(value = "address", layout = ViewCatalog.class)
@PageTitle("Address")
@CssImport("./views/address/address-view.css")
public class AddressView extends Div {
    private AuthResponse authResponse = (AuthResponse) VaadinSession.getCurrent().getAttribute("authResponse");
    private String token = (String) VaadinSession.getCurrent().getAttribute("token");
    private User user;
    private Grid<Address> addressGrid = new Grid<>(Address.class);
    private Button newAddress = new Button("Add new Address");

    public AddressView(UserService userService) throws JsonProcessingException {
        if (authResponse == null) {
            errorPage();

        } else if (authResponse.getUser().getRole().getName().equals("ROLE_ADMIN")) {
            errorPage();
        } else {
            user = authResponse.getUser();
            addClassName("address-view");
            add(createFormLayout());
            add(createGrid(userService));
            add(createButtonLayout());
            ArrayList<Address> addressResponse = userService.getAllAddress(user.getLogin(), token);

            newAddress.addClickListener(buttonClickEvent -> {
                newAddress(userService, addressResponse);

            });
        }

    }

    private Notification notification(String message, int time) {
        return Notification.show(message, time, Notification.Position.MIDDLE);


    }

    private void errorPage() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        Image logo = new Image("images/error.png", "ScooterClient error");
        logo.setHeight("410px");
        logo.setWidth("800px");
        verticalLayout.add(logo);
        add(verticalLayout);
        UI.getCurrent().navigate(ErrorView.class);
    }

    private Component createFormLayout() {
        VerticalLayout wrapper = new VerticalLayout();
        FormLayout formLayout = new FormLayout();
        formLayout.add(addressGrid);
        wrapper.add(formLayout);
        return wrapper;
    }

    private Component createButtonLayout() {
        VerticalLayout buttonLayout = new VerticalLayout();
        buttonLayout.addClassName("button-layout");
        newAddress.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newAddress.setWidth("200px");
        buttonLayout.add(newAddress);
        buttonLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        return buttonLayout;
    }

    private Component createGrid(UserService userService) throws JsonProcessingException {
        ArrayList<Address> addressResponse = userService.getAllAddress(user.getLogin(), token);
        addressGrid.setItems(addressResponse);
        addressGrid.removeAllColumns();

        addressGrid.addColumn(address -> address.getAddress()).setHeader(MESSAGE.get("Addresses"));
        addressGrid.setItems(addressResponse);
        addressGrid.removeAllColumns();
        addressGrid.addColumn(address -> address.getAddress()).setHeader(MESSAGE.get("Addresses"));
        addressGrid.addItemClickListener(event -> {

                    Dialog dialog = new Dialog();
                    Button close = new Button(MESSAGE.get("Cancel"), e -> {
                        dialog.close();
                    });
                    Button delete = new Button(MESSAGE.get("Delete"), e -> {
                        Dialog deleteDialog = new Dialog();
                        deleteDialog.add(new Text(MESSAGE.get("Сonfi")));
                        Button no = new Button("No", buttonClickEvent -> deleteDialog.close());
                        Button yes = new Button("Yes");
                        yes.addClickListener(eventDelete -> {
                            String response = userService.deleteAddress(event.getItem(), token);
                            if (response.equals("OK")) {
                                notification(MESSAGE.get("Done"), 2500);
                                try {
                                    addressGrid.setItems(userService.getAllAddress(user.getLogin(), token));
                                } catch (JsonProcessingException e1) {
                                    e1.getMessage();
                                }

                                deleteDialog.close();
                                dialog.close();
                            } else {
                                notification(MESSAGE.get("Error"), 3000);
                            }
                        });
                        deleteDialog.add(new Div(no, yes));
                        deleteDialog.open();

                    });
                    dialog.add(new Text(event.getItem().getAddress()));
                    dialog.add(new Div(close, delete));
                    dialog.open();
                }
        );
        return addressGrid;
    }

    private void newAddress(UserService userService, ArrayList<Address> addressResponse) {
        Dialog dialog = new Dialog();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        TextField address = new TextField("Address");
      //  address.setPattern("^[a-zA-Zа-яА-Я]+(?:[\\s-][a-zA-Zа-яА-Я]+)*$");
        address.setPreventInvalidInput(true);
        address.setRequired(true);
        horizontalLayout.add(address);

        Button close = new Button(MESSAGE.get("Cancel"), event -> {
            dialog.close();
        });
        Button save = new Button(MESSAGE.get("Save"), event -> {

            Address newAddressValue = new Address();
            newAddressValue.setAddress(address.getValue());

            newAddressValue.setUser(user);
            if (address.getValue() == null) {
                notification(MESSAGE.get("Error"), 4000);
            } else {
                Address response = userService.addUserAddress(newAddressValue, token);
                System.out.println(response);
                System.out.println(address.getValue());
                if (response != null) {
                    notification(MESSAGE.get("Done"), 2500);
                    try {
                        addressGrid.setItems(userService.getAllAddress(user.getLogin(), token));
                    } catch (JsonProcessingException e1) {
                        e1.getMessage();
                    }
                    dialog.close();
                } else {
                    notification(MESSAGE.get("Error"), 4000);
                }
            }


        });
        dialog.add(horizontalLayout);
        dialog.add(new Div(close, save));
        dialog.open();

    }


}