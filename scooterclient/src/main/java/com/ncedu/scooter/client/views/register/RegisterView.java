package com.ncedu.scooter.client.views.register;


import com.ncedu.scooter.client.model.request.user.RegistrationRequest;
import com.ncedu.scooter.client.service.RegistrationService;
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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import static com.ncedu.scooter.client.views.register.Message.MESSAGE;

@CssImport("./views/register/register-view.css")
@Route(value = "register", layout = MainViewAuth.class)
@PageTitle("register")
public class RegisterView extends Div {


    private PhoneNumberField login = new PhoneNumberField("Phone number");
    private TextField address = new TextField("Address");
    private PasswordField password = new PasswordField("Password.At least 6 characters:");

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Register");

    private Binder<RegistrationRequest> binder = new Binder(RegistrationRequest.class);

    public RegisterView(RegistrationService registrationService) {
        addClassName("register-view");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());
        binder.bindInstanceFields(this);
        clearForm();

        cancel.addClickListener(e -> clearForm());
        save.addClickListener(e -> {
            String number = binder.getBean().getLogin().replaceAll(" ", "");
            String password = binder.getBean().getPassword();
            if (number.length() == 12 && number.startsWith("+") && password.length() >= 6) {
                String response = registrationService.registration(binder.getBean());
                if (response.equals("OK")) {
                    notification(MESSAGE.get("Welcome"), 5000);
                    clearForm();
                }
            } else {
                notification(MESSAGE.get("ErrorRegist"), 5000);
            }


        });
    }

    private Notification notification(String message, int time) {
        return Notification.show(message, time, Notification.Position.MIDDLE);
    }
    private void clearForm() {
        binder.setBean(new RegistrationRequest());
    }

    private Component createTitle() {
        return new H3("Personal information");
    }

    private Component createFormLayout() {
        VerticalLayout wrapper = new VerticalLayout();
        FormLayout formLayout = new FormLayout();
        login.setErrorMessage("Please enter a valid phone");
        formLayout.add(login);
        formLayout.add(password);
        formLayout.add(address);
        wrapper.add(formLayout);
        return wrapper;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save);
        buttonLayout.add(cancel);
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


