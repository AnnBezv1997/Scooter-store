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
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ComponentUtil;
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
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;

@Route(value = "catalog", layout = ViewCatalog.class)
@PageTitle("Catalog")
@CssImport("./views/catalog/catalog-view.css")

public class CatalogView extends Div {
    private AuthResponse authResponse = (AuthResponse) VaadinSession.getCurrent().getAttribute("authResponse");
    private String token = (String) VaadinSession.getCurrent().getAttribute("token");
    private User user = authResponse.getUser();
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
        addClassName("catalog-view");
        add(createSearchBar(productService));
        createDataProvider(productService);

        add(createGrid());
        createProductColum();
        createDescriptionColum();
        createCategoryColum();
        createPriceColum();
        addFiltersToGrid(productService);
        Button button = new Button("Next", buttonClickEvent -> {
            if (currentPage < totalPages - 1) {
                currentPage++;
                createDataProvider(productService);
                productGrid.setDataProvider(dataProvider);
            } else {
                Notification.show("The end!", 1000, Notification.Position.MIDDLE);
            }
        });
        Button button1 = new Button("Back", buttonClickEvent -> {
            if (currentPage > 0 && currentPage < totalPages) {
                currentPage--;
                createDataProvider(productService);
                productGrid.setDataProvider(dataProvider);
            } else {
                Notification.show("Beginning!", 1000, Notification.Position.MIDDLE);
            }
        });
        add(button1);
        add(button);

        productGrid.addItemClickListener(event -> {
            Product showProduct = event.getItem();
            pageProduct(showProduct, orderService);

        });

    }

    private Component createTitle() {
        H3 h3 = new H3("Catalog");
        h3.setSizeFull();
        return h3;

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

    private void createProductColum() {
        product = productGrid.addColumn(new ComponentRenderer<>(product -> {
            VerticalLayout hl = new VerticalLayout();
            hl.setAlignItems(FlexComponent.Alignment.AUTO);
            Image logo = new Image("images/logo.png", "ScooterClient logo");
            Span span = new Span();
            span.setClassName("name");
            span.setText(product.getName());
            hl.add(logo, span);
            return hl;
        })).setHeader("Product");
    }

    private void createDescriptionColum() {
        description = productGrid.addColumn(product -> product.getDescription()).setHeader("Description");
    }

    private void createCategoryColum() {
        category = productGrid.addColumn(product -> product.getCategory().getName()).setHeader("Category");

    }

    private void createPriceColum() {
        price = productGrid.addColumn(new ComponentRenderer<>(product -> {
            VerticalLayout hl = new VerticalLayout();
            hl.setAlignItems(FlexComponent.Alignment.START);
            Span span1 = new Span();
            Span span2 = new Span();
            Product product1 = product;
            if (product1.getDiscount() != null) {
                if (product1.getDiscount().getDiscountType().toString().equals("ABSOLUTE")) {
                    BigDecimal p = product1.getPrice().subtract(product1.getDiscount().getValue()).setScale(0, RoundingMode.HALF_UP);
                    span1.setText("New price:" + p + "P.");
                    span2.setText("Old price:" + product1.getPrice() + "P.");
                } else {
                    BigDecimal p = product1.getPrice().subtract(product1.getPrice().multiply(new BigDecimal(product1.getDiscount().getValue().doubleValue() / 100))).setScale(0, RoundingMode.HALF_UP);
                    span1.setText("New price:" + p + "P.");
                    span2.setText("Old price:" + product1.getPrice() + "P.");
                }

                hl.add(span1, span2);
                return hl;
            } else {
                span1.setText(product1.getPrice() + "P.");
                hl.add(span1);
                return hl;
            }

        })).setHeader("Price");
    }

    private Component createSearchBar(ProductService productService) {
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

    private void pageProduct(Product showProduct, OrderService orderService) {
        Dialog dialog = new Dialog();
        dialog.setWidth("600px");

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        Image logo = new Image("images/logo.png", "ScooterClient logo");
        logo.setHeight("450px");
        logo.setWidth("450px");
        Span name = new Span();
        Span description = new Span();
        Span category = new Span();
        Span descriptionCategory = new Span();
        Span price = new Span();
        Span stockStatus = new Span();

        name.setText("Name : " + showProduct.getName());
        description.setText("Description : " + showProduct.getDescription());
        category.setText("Category : " + showProduct.getCategory().getName());
        descriptionCategory.setText(showProduct.getCategory().getDescription());
        BigDecimal p;
        if (showProduct.getDiscount() != null) {
            if (showProduct.getDiscount().getDiscountType().toString().equals("ABSOLUTE")) {
                p = showProduct.getPrice().subtract(showProduct.getDiscount().getValue()).setScale(0, RoundingMode.HALF_UP);
                price.setText("Price : " + p + "Р");
            } else {
                p = showProduct.getPrice().subtract(showProduct.getPrice().multiply(new BigDecimal(showProduct.getDiscount().getValue().doubleValue() / 100))).setScale(0, RoundingMode.HALF_UP);
                price.setText("Price : " + p + "Р");
            }

        } else {
            p = showProduct.getPrice();
            price.setText("Price : " + p.doubleValue() + "Р");
        }

        if (showProduct.getStockStatus().getCount() > 0) {
            stockStatus.setText("In stock");
        } else {
            stockStatus.setText("Out of stock");
        }
        verticalLayout.add(logo, name, description, category, descriptionCategory, price, stockStatus);
        Button close = new Button("Cancel", e -> {
            dialog.close();
        });
        if (!user.getRole().getName().equals("ROLE_ADMIN")) {
            NumberField countProduct = new NumberField();
            countProduct.setValue(1d);
            countProduct.setHasControls(true);
            countProduct.setMin(1);
            countProduct.setMax(showProduct.getStockStatus().getCount());

            Button addToBasket = new Button("Add to basket", e -> {
                Basket basket = new Basket();
                basket.setUserId(user.getId());
                basket.setUserOrder(null);
                basket.setProductId(showProduct.getId());
                basket.setPrice(new BigDecimal(p.doubleValue()));
                basket.setCountProduct(countProduct.getValue().intValue());
                basket.setDate(new Date());
                boolean add = orderService.addProductBasket(basket, token);
                if (add) {
                    Notification.show("Product added, go to the shopping cart!", 1000, Notification.Position.MIDDLE);
                }
            });
            dialog.add(new Div(addToBasket, countProduct));
        }

        dialog.add(verticalLayout);
        dialog.add(new Div(close));
        dialog.open();
    }

    private Component createButtonLayout() {
        VerticalLayout buttonLayout = new VerticalLayout();
        buttonLayout.addClassName("button-layout");
        buttonLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        return buttonLayout;
    }

    private void addFiltersToGrid(ProductService productService) throws JsonProcessingException {
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
        sort.add("Ascending");
        sort.add("Descending");
        sort.add("First with discount");
        priceFilter.setItems(sort);
        priceFilter.setPlaceholder("Sorting");
        priceFilter.setClearButtonVisible(true);
        priceFilter.setWidth("100%");
        priceFilter.addValueChangeListener(
                event -> {
                    if (event.getValue() != null) {
                        if (event.getValue().equals("Ascending")) {
                            sortDirection = "ASC";
                            sortBy = "price";
                            currentPage = 0;
                            createDataProvider(productService);
                            productGrid.setDataProvider(dataProvider);
                        } else if (event.getValue().equals("Descending")) {
                            sortDirection = "DESC";
                            sortBy = "price";
                            currentPage = 0;
                            createDataProvider(productService);
                            productGrid.setDataProvider(dataProvider);
                        } else if (event.getValue().equals("First with discount")) {
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



