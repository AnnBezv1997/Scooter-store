package com.ncedu.scooter.client.views.main;

import com.ncedu.scooter.client.model.request.user.AuthResponse;
import com.ncedu.scooter.client.views.address.AddressView;
import com.ncedu.scooter.client.views.catalog.CatalogView;
import com.ncedu.scooter.client.views.catalog.CatalogViewAdmin;
import com.ncedu.scooter.client.views.login.LoginView;
import com.ncedu.scooter.client.views.order.BasketView;
import com.ncedu.scooter.client.views.order.OrderView;
import com.ncedu.scooter.client.views.register.RegisterView;
import com.ncedu.scooter.client.views.user.UserView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;

import java.util.Optional;

@JsModule("./styles/shared-styles.js")
@CssImport("./views/main/main-view-new.css")

public class ViewCatalog extends AppLayout {

    private final Tabs menu;
    private H1 viewTitle;

    public ViewCatalog() {
        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        menu = createMenu();
        addToDrawer(createDrawerContent(menu));
    }

    private Component createHeaderContent() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setId("header");
        layout.getThemeList().set("dark", true);
        layout.setWidthFull();
        layout.setSpacing(false);
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.add(new DrawerToggle());
        viewTitle = new H1();
       // layout.add(viewTitle);
        layout.add(new Avatar());
        return layout;
    }

    private Component createDrawerContent(Tabs menu) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        layout.getThemeList().set("spacing-s", true);
        layout.setAlignItems(FlexComponent.Alignment.STRETCH);
        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.add(new Image("images/logo.png", "My App logo"));
        logoLayout.add(new H1("My App"));
        layout.add(logoLayout, menu);
        return layout;
    }

    private Tabs createMenu() {
        AuthResponse authResponse = (AuthResponse) VaadinSession.getCurrent().getAttribute("authResponse");
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setId("tabs");
            tabs.add(createMenuItems());

        return tabs;
    }

    private Component[] createMenuItems() {
        AuthResponse authResponse = (AuthResponse) VaadinSession.getCurrent().getAttribute("authResponse");
        if (authResponse == null || authResponse.getUser().getRole().equals("ROLE_USER")) {
            return new Tab[]{createTab("Login", LoginView.class), createTab("Registration", RegisterView.class)};
        } else if (authResponse.getUser().getRole().getName().equals("ROLE_ADMIN")) {

            return new Tab[]{createTab("Catalog Settings", CatalogViewAdmin.class), createTab("Catalog user", CatalogView.class),
                    createTab("Log out", LoginView.class)};

        } else {
            return new Tab[]{createTab("Catalog products", CatalogView.class), createTab("User settings", UserView.class),
                    createTab("Address", AddressView.class), createTab("Basket", BasketView.class),
                    createTab("Order", OrderView.class)};

        }
    }



    private static Tab createTab(String text, Class<? extends Component> navigationTarget) {
        final Tab tab = new Tab();
        tab.add(new RouterLink(text, navigationTarget));
        ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        getTabForComponent(getContent()).ifPresent(menu::setSelectedTab);
        viewTitle.setText(getCurrentPageTitle());
    }

    private Optional<Tab> getTabForComponent(Component component) {
        return menu.getChildren().filter(tab -> ComponentUtil.getData(tab, Class.class).equals(component.getClass()))
                .findFirst().map(Tab.class::cast);
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
