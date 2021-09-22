package com.ncedu.scooter.client.views.catalog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ncedu.scooter.client.model.order.Basket;
import com.ncedu.scooter.client.model.product.Category;
import com.ncedu.scooter.client.model.product.Product;
import com.ncedu.scooter.client.model.request.product.ProductRequest;
import com.ncedu.scooter.client.model.request.product.ProductResponse;
import com.ncedu.scooter.client.model.request.user.AuthResponse;
import com.ncedu.scooter.client.model.user.User;
import com.ncedu.scooter.client.service.OrderService;
import com.ncedu.scooter.client.service.ProductService;
import com.ncedu.scooter.client.views.main.ViewCatalog;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.dom.DebouncePhase;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import static com.ncedu.scooter.client.views.catalog.Message.MESSAGE;

@Route(value = "catalog", layout = ViewCatalog.class)
@PageTitle("Catalog")
@CssImport("./views/catalog/catalog-view.css")

public class CatalogView extends Div {
    private AuthResponse authResponse = (AuthResponse) VaadinSession.getCurrent().getAttribute("authResponse");
    private String token = (String) VaadinSession.getCurrent().getAttribute("token");
    private User user;
    private Grid<Product> productGrid = new Grid<>(Product.class);
    private ListDataProvider<Product> dataProvider;
    private Grid.Column<Product> product;
    private Grid.Column<Product> description;
    private Grid.Column<Product> category;
    private Grid.Column<Product> price;
    private TextField searchBar = new TextField();

    private Integer currentPage = 0;
    private int totalPages;
    private String sortDirection = ""; //true по возрастанию false по убыванию
    private Integer categorySort = null; //Категория для стортировки id
    private String sortBy = ""; //по цене
    private String search = "";  // если был введено в поиск что то

    private Integer sizeElement = 2;

    public CatalogView(ProductService productService, OrderService orderService) throws JsonProcessingException {
        if (authResponse == null) {
            errorPage();

        }else {
            user = authResponse.getUser();
            addClassName("catalog-view");
            add(createSearchBar(productService));
            createDataProvider(productService);

            add(createGrid());
            createProductColum();
            createDescriptionColum();
            createCategoryColum();
            createPriceColum();
            addFiltersToGrid(productService);
            Button button = new Button(MESSAGE.get("Next"), buttonClickEvent -> {
                if (currentPage < totalPages - 1) {
                    currentPage++;
                    createDataProvider(productService);
                    productGrid.setDataProvider(dataProvider);
                } else {
                    notification(MESSAGE.get("The end"), 1200);

                }
            });
            Button button1 = new Button(MESSAGE.get("Back"), buttonClickEvent -> {
                if (currentPage > 0 && currentPage < totalPages) {
                    currentPage--;
                    createDataProvider(productService);
                    productGrid.setDataProvider(dataProvider);
                } else {
                    notification(MESSAGE.get("Go"), 1000);
                }
            });
            add(button1);
            add(button);

            productGrid.addItemClickListener(event -> {
                Product showProduct = event.getItem();
                pageProduct(showProduct, orderService,productService);
                createDataProvider(productService);
                productGrid.setDataProvider(dataProvider);
                productGrid.getDataProvider().refreshAll();
            });
        }

    }
    private Notification notification(String message, int time) {
        return Notification.show(message, time, Notification.Position.MIDDLE);
    }
    public void errorPage() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        Image logo = new Image("images/error.png", "ScooterClient error");
        logo.setHeight("410px");
        logo.setWidth("800px");
        verticalLayout.add(logo);
        add(verticalLayout);
        UI.getCurrent().navigate(ErrorView.class);
    }

    private void createDataProvider(ProductService productService) {
        ProductRequest productRequest = new ProductRequest(currentPage, sizeElement, sortBy, sortDirection, categorySort, search);
        ProductResponse productResponse = productService.getPageProduct(token, productRequest);
        totalPages = productResponse.getTotalPages();
        dataProvider = new ListDataProvider<>(productResponse.getProductList());
    }

    private Component createGrid() {
        productGrid.setHeight("82%");
        productGrid.setDataProvider(dataProvider);
        productGrid.removeAllColumns();
        return productGrid;
    }

    public void createProductColum() {
        product = productGrid.addColumn(new ComponentRenderer<>(product -> {
            VerticalLayout hl = new VerticalLayout();
            hl.setAlignItems(FlexComponent.Alignment.AUTO);
            Image logo = new Image("images/" + product.getImage(), "ScooterClient logo");
            logo.setHeight("150px");
            logo.setHeight("150px");
            Span span = new Span();
            span.setClassName("name");
            span.setText(product.getName());
            hl.add(logo, span);
            return hl;
        })).setHeader(MESSAGE.get("Product"));
    }

    private void createDescriptionColum() {
        description = productGrid.addColumn(product -> product.getDescription()).setHeader(MESSAGE.get("Description"));
    }

    public void createCategoryColum() {
        category = productGrid.addColumn(new ComponentRenderer<>(product -> {
            VerticalLayout hl = new VerticalLayout();
            hl.setAlignItems(FlexComponent.Alignment.START);
            Span span1 = new Span();
            Product product1 = product;
            if(product1.getCategory() == null){
                span1.setText("");
                hl.add(span1);
                return hl;
            }else {
                span1.setText(product1.getCategory().getName());
                hl.add(span1);
                return hl;
            }
        })).setHeader(MESSAGE.get("Category"));
    }

    private void createPriceColum() {
        price = productGrid.addColumn(new ComponentRenderer<>(product -> {
            VerticalLayout hl = new VerticalLayout();
            hl.setAlignItems(FlexComponent.Alignment.START);
            Span span1 = new Span();
            Span span2 = new Span();
            Product p = product;
            if (p.getDiscount() != null) {
                if (p.getDiscount().getDiscountType().toString().equals("ABSOLUTE")) {
                    double totalDiscount = p.getDiscount().getValue().doubleValue();
                    BigDecimal price = p.getPrice().subtract(new BigDecimal(totalDiscount)).setScale(1,BigDecimal.ROUND_HALF_UP);
                    span1.setText(MESSAGE.get("New price") + price + " $.");
                    span2.setText(MESSAGE.get("Old price") + p.getPrice() + " $.");
                } else {
                    double totalDiscount = p.getPrice().multiply(new BigDecimal(p.getDiscount().getValue().doubleValue() / 100)).doubleValue();
                    BigDecimal price = p.getPrice().subtract(new BigDecimal(totalDiscount)).setScale(1,BigDecimal.ROUND_HALF_UP);
                    span1.setText(MESSAGE.get("New price") + price + " $.");
                    span2.setText(MESSAGE.get("Old price") + p.getPrice() + " $.");
                }

                hl.add(span1, span2);
                return hl;
            } else {
                span1.setText(p.getPrice() + " $.");
                hl.add(span1);
                return hl;
            }

        })).setHeader("Price");
    }

    public Component createSearchBar(ProductService productService) {
        searchBar.setPlaceholder("Search...");
        searchBar.setWidth("1190px");
        searchBar.setValueChangeMode(ValueChangeMode.EAGER);
        searchBar.setPrefixComponent(VaadinIcon.SEARCH.create());
        Icon closeIcon = new Icon("lumo", "cross");
        closeIcon.setVisible(false);
        ComponentUtil.addListener(closeIcon, ClickEvent.class,
                (ComponentEventListener) e -> {
                    searchBar.clear();
                    search = "";
                    currentPage = 0;
                    createDataProvider(productService);
                    productGrid.setDataProvider(dataProvider);
                });
        searchBar.setSuffixComponent(closeIcon);
        searchBar.getElement().addEventListener("value-changed", event -> {
            closeIcon.setVisible(!searchBar.getValue().isEmpty());
            if (searchBar.getValue() != null) {
                search = searchBar.getValue();
                currentPage = 0;
                createDataProvider(productService);
                productGrid.setDataProvider(dataProvider);
            }


        }).debounce(300, DebouncePhase.TRAILING);
        return searchBar;
    }

    private void pageProduct(Product showProduct, OrderService orderService,ProductService productService) {
        PageProduct pageProduct = new PageProduct(orderService,showProduct,user,token);
        pageProduct.pageProduct();
        createDataProvider(productService);
        productGrid.setDataProvider(dataProvider);
    }


    public void addFiltersToGrid(ProductService productService) throws JsonProcessingException {
        HeaderRow filterRow = productGrid.appendHeaderRow();
        ArrayList<Category> categoryArrayList = productService.getCategories(token);

        ComboBox<Category> categoryFilter = new ComboBox<>();
        categoryFilter.setItemLabelGenerator(Category::getName);
        categoryFilter.setItems(categoryArrayList);
        categoryFilter.setPlaceholder("Select a category");
        categoryFilter.setClearButtonVisible(true);
        categoryFilter.setWidth("100%");
        categoryFilter.addValueChangeListener(
                event -> {
                    if (event.getValue() != null) {
                        categorySort = event.getValue().getId();
                        createDataProvider(productService);
                        productGrid.setDataProvider(dataProvider);
                    } else {
                        categorySort = null;
                        currentPage = 0;
                        createDataProvider(productService);
                        productGrid.setDataProvider(dataProvider);
                    }

                });
        filterRow.getCell(category).setComponent(categoryFilter);

        ComboBox<String> priceFilter = new ComboBox<>();
        ArrayList<String> sort = new ArrayList<>();
        sort.add(MESSAGE.get("Asc"));
        sort.add(MESSAGE.get("Desc"));
        sort.add(MESSAGE.get("FirstDiscount"));
        priceFilter.setItems(sort);
        priceFilter.setPlaceholder("Sorting");
        priceFilter.setClearButtonVisible(true);
        priceFilter.setWidth("100%");
        priceFilter.addValueChangeListener(
                event -> {
                    if (event.getValue() != null) {
                        if (event.getValue().equals(MESSAGE.get("Asc"))) {
                            sortDirection = "ASC";
                            sortBy = "price";
                            currentPage = 0;
                            createDataProvider(productService);
                            productGrid.setDataProvider(dataProvider);
                        } else if (event.getValue().equals(MESSAGE.get("Desc"))) {
                            sortDirection = "DESC";
                            sortBy = "price";
                            currentPage = 0;
                            createDataProvider(productService);
                            productGrid.setDataProvider(dataProvider);
                        } else if (event.getValue().equals(MESSAGE.get("FirstDiscount"))) {
                            sortDirection = "ASC";
                            sortBy = "discount";
                            currentPage = 0;
                            createDataProvider(productService);
                            productGrid.setDataProvider(dataProvider);
                        }
                    } else {
                        sortDirection = "";
                        sortBy = "";
                        currentPage = 0;
                        createDataProvider(productService);
                        productGrid.setDataProvider(dataProvider);
                    }

                });
        filterRow.getCell(price).setComponent(priceFilter);
    }

}



