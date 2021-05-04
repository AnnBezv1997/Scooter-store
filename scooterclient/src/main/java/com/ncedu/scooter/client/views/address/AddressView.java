package com.ncedu.scooter.client.views.address;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ncedu.scooter.client.model.user.Address;
import com.ncedu.scooter.client.model.request.user.AddressRequest;
import com.ncedu.scooter.client.model.request.user.AuthResponse;
import com.ncedu.scooter.client.model.user.User;
import com.ncedu.scooter.client.service.UserService;
import com.ncedu.scooter.client.views.main.ViewCatalog;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.util.ArrayList;


@Route(value = "address", layout = ViewCatalog.class)
@PageTitle("Address")
@CssImport("./views/address/address-view.css")
public class AddressView extends Div {
    private AuthResponse authResponse = (AuthResponse) VaadinSession.getCurrent().getAttribute("authResponse");
    private String token = (String) VaadinSession.getCurrent().getAttribute("token");
    private User user = authResponse.getUser();
    private Grid<Address> addressGrid = new Grid<>(Address.class);
    private Button newAddress = new Button("Add new Address");

    public AddressView(UserService userService) throws JsonProcessingException {
        addClassName("address-view");
        add(createFormLayout());
        add(addressGrid);
        add(createButtonLayout());
        ArrayList<Address> addressResponse = userService.getAllAddress(user.getLogin(), token);
        addressGrid.setItems(addressResponse);
        addressGrid.removeAllColumns();
        addressGrid.addColumn(address -> address.getAddress()).setHeader("Addresses");

        addressGrid.addItemClickListener(event -> {

                    Dialog dialog = new Dialog();
                    Button close = new Button("Cancel", e -> {
                        dialog.close();
                    });
                    Button delete = new Button("Delete", e -> {
                        Dialog deleteDialog = new Dialog();
                        deleteDialog.add(new Text("Are you sure?"));
                        Button no = new Button("No", buttonClickEvent -> deleteDialog.close());
                        Button yes = new Button("Yes, detele");
                        yes.addClickListener(eventDelete -> {
                            String response = userService.deleteAddress(event.getItem(), token);
                            if (response.equals("OK")) {
                                Notification.show("Done!", 1000, Notification.Position.MIDDLE);
                                addressResponse.remove(event.getItem());
                                addressGrid.setItems(addressResponse);
                                deleteDialog.close();
                                dialog.close();
                            } else {
                                Notification.show("An error occurred.Try again.", 1500, Notification.Position.MIDDLE);

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
        newAddress.addClickListener(buttonClickEvent -> {
            Dialog dialog = new Dialog();
            TextField newAddress = new TextField("New Address");
            Button close = new Button("Cancel", event -> {

                dialog.close();
            });
            Button save = new Button("Save", event -> {

                String newAddressValue = newAddress.getValue();
                Address response = userService.addUserAddress(new AddressRequest(newAddressValue, user.getLogin()), token);
                if (response != null) {
                    Notification.show("Done!", 2500, Notification.Position.MIDDLE);
                    addressResponse.add(response);
                    addressGrid.setItems(addressResponse);
                    dialog.close();
                } else {
                    Notification.show("An error occurred.Try again.", 4000, Notification.Position.MIDDLE);

                }

            }
            );
            dialog.add(new Text("Add your name"));
            dialog.add(new Div(newAddress));
            dialog.add(new Div(close, save));
            dialog.open();

        });
    }

    private Component createTitle() {
        H3 h3 = new H3("Addresss");
        h3.setSizeFull();
        return h3;

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
        addressGrid.addColumn(address -> address.getAddress()).setHeader("Addresses");
        return addressGrid;
    }


}