package com.ncedu.scooter.client.views.register;


import com.ncedu.scooter.client.model.request.user.RegistrationRequest;
import com.ncedu.scooter.client.model.user.Address;
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
import com.vaadin.flow.component.orderedlayout.FlexComponent;
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
    private PasswordField password = new PasswordField("Password.At least 6 characters:");

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Register");


    private TextField address = new TextField("Address");




    public RegisterView(RegistrationService registrationService) {
        addClassName("register-view");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());

        address.setPattern("^[a-zA-Zа-яА-Я]+(?:[\\s-][a-zA-Zа-яА-Я]+)*$");
     //   address.setRequired(false);


        cancel.addClickListener(e -> clearForm());
        save.addClickListener(e -> {

            String number = login.getValue().replaceAll(" ", "");
            String passwordUser = password.getValue();

            Address userAddress = new Address();
            if(!address.isEmpty()){
                userAddress.setAddress(address.getValue());

            }else{
                userAddress = null;
            }
            if (10 <= number.length() && number.length() <= 14 && number.startsWith("+") && passwordUser.length() >= 6) {
                String userNumber = number.replaceAll("\\+","");
                String response = registrationService.registration(new RegistrationRequest(userNumber,passwordUser,userAddress));
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
       login.clear();
       password.clear();
       address.clear();
    }

    private Component createTitle() {
        return new H3("Personal information");
    }

    private Component createFormLayout() {
        VerticalLayout wrapper = new VerticalLayout();
        wrapper.setAlignItems(FlexComponent.Alignment.CENTER);
        login.setWidth("500px");
        password.setWidth("500px");
        address.setWidth("500px");
        login.setErrorMessage("Please enter a valid phone");
        wrapper.add(login);
        wrapper.add(password);
        wrapper.add(password);
        wrapper.add(address);

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

            countryCode.setWidth("140px");
            countryCode.setPlaceholder("+");
            countryCode.setPattern("\\+\\d*");
            countryCode.setPreventInvalidInput(true);
            countryCode.setRequired(true);
            //countryCode.setItems("+","+7");
            countryCode.addCustomValueSetListener(e -> countryCode.setValue(e.getDetail()));

            number.setPattern("\\d*");
            number.setRequired(true);
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


