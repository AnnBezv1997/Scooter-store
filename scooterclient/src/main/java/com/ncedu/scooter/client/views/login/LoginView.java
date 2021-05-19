package com.ncedu.scooter.client.views.login;

import com.ncedu.scooter.client.model.user.User;
import com.ncedu.scooter.client.model.request.user.AuthRequest;
import com.ncedu.scooter.client.model.request.user.AuthResponse;
import com.ncedu.scooter.client.service.AuthService;
import com.ncedu.scooter.client.views.main.MainViewAuth;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import static com.ncedu.scooter.client.views.login.Message.MESSAGE;


@CssImport("./views/login/login-view.css")
@Route(value = "", layout = MainViewAuth.class)
@PageTitle("Login")
public class LoginView extends Div {

    private PhoneNumberField login = new PhoneNumberField("Phone number");
    private PasswordField password = new PasswordField("Password");
    private Button cancel = new Button("Cancel");
    private Button save = new Button("Login");

    private Binder<AuthRequest> binder = new Binder(AuthRequest.class);

    public LoginView(AuthService authService) {
        addClassName("login-view");
        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());
        binder.bindInstanceFields(this);
        clearForm();

        cancel.addClickListener(e -> clearForm());
        save.addClickListener(e -> {

            AuthResponse authResponse = authService.auth(binder.getBean());

            if (authResponse != null) {
                Notification.show("Welcome!", 4000, Notification.Position.MIDDLE);

                VaadinSession.getCurrent().setAttribute("authResponse", authResponse);
                VaadinSession.getCurrent().setAttribute("token", authResponse.getToken());
                VaadinSession.getCurrent().getSession().setMaxInactiveInterval(3600); //длительность сессии час

                User user = authResponse.getUser();
                save.getUI().ifPresent(ui -> {
                    if (user.getRole().getName().equals("ROLE_ADMIN")) {
                        ui.navigate("admin");
                    } else if(user.getRole().getName().equals("ROLE_USER")){
                        ui.navigate("catalog");
                    }else {
                        ui.navigate("errorforbidden");
                    }

                    //здесь будет переходить на страницу каталога
                });
                clearForm();
            } else {

                notification(MESSAGE.get("Error"), 5000);
                clearForm();
            }


        });
    }
    private Notification notification(String message, int time) {
        return Notification.show(message, time, Notification.Position.MIDDLE);
    }
    private void clearForm() {
        binder.setBean(new AuthRequest());
    }

    private Component createTitle() {
        H3 h3 = new H3("Enter your phone number and password");
        h3.setSizeFull();
        return h3;

    }

    private Component createFormLayout() {
        VerticalLayout wrapper = new VerticalLayout();
        FormLayout formLayout = new FormLayout();
        login.setErrorMessage("Please enter a valid phone");
        formLayout.add(login);
        formLayout.add(password);
        wrapper.add(formLayout);
        return wrapper;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save);
        buttonLayout.add(cancel);
        buttonLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        return buttonLayout;
    }

    private static class PhoneNumberField extends CustomField<String> {
        private ComboBox<String> countryCode = new ComboBox<>();
        private TextField number = new TextField();

        PhoneNumberField(String label) {
            setLabel(label);
            countryCode.setWidth("120px");
            countryCode.setPlaceholder("Country");
            countryCode.setPattern("\\+\\d*");
            countryCode.setPreventInvalidInput(true);
            countryCode.setItems("+7");
            countryCode.addCustomValueSetListener(e -> countryCode.setValue(e.getDetail()));

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
