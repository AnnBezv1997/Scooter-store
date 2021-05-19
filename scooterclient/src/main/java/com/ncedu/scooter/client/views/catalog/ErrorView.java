package com.ncedu.scooter.client.views.catalog;

import com.ncedu.scooter.client.views.main.ViewCatalog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "errorforbidden", layout = ViewCatalog.class)
@PageTitle("Error")

public class ErrorView extends Div {
    public ErrorView() {
        addClassName("error-view");
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        Image logo = new Image("images/error.png" , "ScooterClient error");
        logo.setHeight("410px");
        logo.setWidth("800px");
        verticalLayout.add(logo);
        add(verticalLayout);
    }
}
