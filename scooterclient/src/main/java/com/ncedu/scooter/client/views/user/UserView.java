package com.ncedu.scooter.client.views.user;

import com.ncedu.scooter.client.model.User;
import com.ncedu.scooter.client.model.request.AuthRequest;
import com.ncedu.scooter.client.model.request.AuthResponse;
import com.ncedu.scooter.client.model.request.NameAddRequest;
import com.ncedu.scooter.client.model.request.UpdateLoginRequest;
import com.ncedu.scooter.client.service.AuthService;
import com.ncedu.scooter.client.service.UserService;
import com.ncedu.scooter.client.views.main.ViewCatalog;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;


@Route(value = "user", layout = ViewCatalog.class)
@PageTitle("User Settings")
@CssImport("./views/user/user-view.css")
public class UserView extends Div {
    private AuthResponse authResponse = (AuthResponse) VaadinSession.getCurrent().getAttribute("authResponse");
    private String token = (String) VaadinSession.getCurrent().getAttribute("token");
    private User user = authResponse.getUser();
    private Button phones = new Button("Your phone number: " + user.getLogin() + ". Click to change");
    private Button name = new Button("Your name: " + user.getName() + ". Click to change");
    private Button address = new Button("Address");
    private Button exit = new Button("Log out");

    public UserView(UserService userService, AuthService authService) {
        addClassName("user-view");
        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());
        phones.addClickListener(e -> {

            Dialog dialog = new Dialog();
            PhoneNumberField phone = new PhoneNumberField("New phone number");
            PasswordField password = new PasswordField("Confirm your password");
            password.setWidth("350px");
            Button close = new Button("Cancel", event -> {
                dialog.close();
            });
            Button save = new Button("Save", event -> {
                String newPhone = phone.getValue();
                String pass = password.getValue();
                boolean response = userService.updateUserLogin(new UpdateLoginRequest(user.getLogin(), newPhone), token);
                if (response) {
                    AuthResponse authResponse = authService.auth(new AuthRequest(newPhone, pass));
                    VaadinSession.getCurrent().setAttribute("authResponse", authResponse);
                    VaadinSession.getCurrent().setAttribute("token", authResponse.getToken());
                    user = authResponse.getUser();
                    token = authResponse.getToken();
                    phones.setText("Your phone number: " + user.getLogin() + ". Click to change");
                    dialog.close();
                } else {
                    Notification.show("An error occurred.Try again.", 1500, Notification.Position.MIDDLE);

                }

            }
            );
            dialog.add(new Text("Add your new phone number"));
            dialog.add(new Div(phone));
            dialog.add(new Div(password));
            dialog.add(new Div(close, save));
            dialog.open();


        });


        name.addClickListener(e -> {

            Dialog dialog = new Dialog();
            TextField newNameSub = new TextField("Name");
            Button close = new Button("Cancel", event -> {

                dialog.close();
            });
            Button save = new Button("Save", event -> {

                String newName = newNameSub.getValue();
                User response = userService.addUserName(new NameAddRequest(newName, user.getLogin()), token);
                if (response != null) {
                    user.setName(newName);
                    name.setText("Your name: " + response.getName() + ". Click to change");
                    dialog.close();
                } else {
                    Notification.show("An error occurred.Try again.", 4000, Notification.Position.MIDDLE);

                }
            }
            );
            dialog.add(new Text("Add your name"));
            dialog.add(new Div(newNameSub));
            dialog.add(new Div(close, save));
            dialog.open();

        });
        address.addClickListener(e -> {
            address.getUI().ifPresent(ui -> {
                ui.navigate("address");
            });
        });
        exit.addClickListener(e -> {

            Dialog dialog = new Dialog();
            Button close = new Button("Cancel", event -> {
                dialog.close();
            });
            Button yes = new Button("Yes");
            yes.addClickListener(event -> {
                        dialog.close();
                        yes.getUI().ifPresent(ui -> {
                            ui.navigate("login");
                        });
                    }
            );
            dialog.add(new Text("Are you sure you want to get out?"));
            dialog.add(new Div(close, yes));
            dialog.open();


        });

    }

    private Component createTitle() {
        H3 h3 = new H3("User settings");
        h3.setSizeFull();
        return h3;

    }

    private Component createFormLayoutDialog(PhoneNumberField phoneNumberField, PasswordField passwordField) {
        VerticalLayout wrapper = new VerticalLayout();
        FormLayout formLayout = new FormLayout();
        phoneNumberField.setErrorMessage("Please enter a valid phone");
        formLayout.add(phoneNumberField);
        formLayout.add(passwordField);
        wrapper.add(formLayout);
        return wrapper;
    }

    private Component createFormLayout() {
        VerticalLayout wrapper = new VerticalLayout();
        FormLayout formLayout = new FormLayout();
        wrapper.add(formLayout);
        return wrapper;
    }

    private Component createButtonLayout() {
        VerticalLayout buttonLayout = new VerticalLayout();
        buttonLayout.addClassName("button-layout");
        phones.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        name.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        address.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        phones.setWidth("500px");
        name.setWidth("500px");
        address.setWidth("500px");
        buttonLayout.add(phones);
        buttonLayout.add(name);
        buttonLayout.add(address);
        buttonLayout.add(exit);
        buttonLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        return buttonLayout;
    }

    private static class PhoneNumberField extends CustomField<String> {
        private ComboBox<String> countryCode = new ComboBox<>();
        private TextField number = new TextField();

        PhoneNumberField(String label) {
            setLabel(label);
            countryCode.setWidth("90px");
            countryCode.setPlaceholder("Country");
            countryCode.setPattern("\\+\\d*");
            countryCode.setPreventInvalidInput(true);
            countryCode.setItems("+7");
            countryCode.addCustomValueSetListener(e -> countryCode.setValue(e.getDetail()));
            number.setWidth("245px");
            number.setPattern("\\d*");
            number.setPreventInvalidInput(true);
            HorizontalLayout layout = new HorizontalLayout(countryCode, number);
            layout.setFlexGrow(1.0, number);
            add(layout);
        }

        @Override
        protected String generateModelValue() {
            if (countryCode.getValue() != null && number.getValue() != null) {
                return countryCode.getValue() + number.getValue();
            }
            return "";
        }

        @Override
        protected void setPresentationValue(String phoneNumber) {
            String[] parts = phoneNumber != null ? phoneNumber.split(" ", 2) : new String[0];
            if (parts.length == 1) {
                countryCode.clear();
                number.setValue(parts[0]);
            } else if (parts.length == 2) {
                countryCode.setValue(parts[0]);
                number.setValue(parts[1]);
            } else {
                countryCode.clear();
                number.clear();
            }
        }
    }
}
