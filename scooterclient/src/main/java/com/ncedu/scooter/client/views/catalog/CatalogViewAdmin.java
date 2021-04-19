package com.ncedu.scooter.client.views.catalog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ncedu.scooter.client.model.*;
import com.ncedu.scooter.client.model.request.AuthResponse;
import com.ncedu.scooter.client.model.request.ProductRequest;
import com.ncedu.scooter.client.model.request.ProductResponse;
import com.ncedu.scooter.client.service.ProductServiceAdmin;
import com.ncedu.scooter.client.views.main.ViewCatalog;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.dom.DebouncePhase;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.math.BigDecimal;
import java.util.ArrayList;

@Route(value = "admin", layout = ViewCatalog.class)
@PageTitle("Admin")
@CssImport("./views/catalog/catalog-view-admin.css")
public class CatalogViewAdmin extends Div {
    private AuthResponse authResponse = (AuthResponse) VaadinSession.getCurrent().getAttribute("authResponse");
    private String token = (String) VaadinSession.getCurrent().getAttribute("token");
    private User user = authResponse.getUser();
    private GridPro<Product> productGrid = new GridPro<>(Product.class);
    private Grid<Discount> discountGrid = new Grid<>();
    private Grid<Category> categoryGrid = new Grid<>();
    private ListDataProvider<Product> dataProvider;
    private Grid.Column<Product> product;
    private Grid.Column<Product> description;
    private Grid.Column<Product> category;
    private Grid.Column<Product> price;
    private Grid.Column<Product> discount;
    private Grid.Column<Product> id;
    private Grid.Column<Product> stockStatus;
    private TextField searchBar = new TextField();

    private Integer currentPage = 0;
    private int totalPages;
    private String sortDirection = "ASC"; //true по возрастанию false по убыванию
    private Integer categorySort = null; //Категория для стортировки id
    private String sortBy = "id"; //по цене
    private String search = "";  // если был введено в поиск что то

    private Integer sizeElement = 2;

    public CatalogViewAdmin(ProductServiceAdmin productService) throws JsonProcessingException {

        addClassName("catalog-view-admin");
        add(createSearchBar(productService));
        createDataProvider(productService);
        add(createGrid());
        createIdColumn();
        createProductColum();
        createDescriptionColum();
        createCategoryColum();
        createPriceColum();
        createDisconutColum();
        createStockStatusColum();
        addFiltersToGrid(productService);
        Button next = new Button("Next", buttonClickEvent -> {
            if (currentPage < totalPages - 1) {
                currentPage++;
                createDataProvider(productService);
                productGrid.setDataProvider(dataProvider);
            } else {
                Notification.show("The end!", 1000, Notification.Position.MIDDLE);
            }
        });
        Button back = new Button("Back", buttonClickEvent -> {
            if (currentPage > 0 && currentPage < totalPages) {
                currentPage--;
                createDataProvider(productService);
                productGrid.setDataProvider(dataProvider);
            } else {
                Notification.show("Beginning!", 1000, Notification.Position.MIDDLE);
            }
        });
        add(back);
        add(next);
        productGrid.addItemClickListener(event -> {
            Product showProduct = event.getItem();
            try {
                pageProduct(showProduct, productService);
            } catch (JsonProcessingException e) {
                e.getMessage();
            }


        });

    }

    private void createDataProvider(ProductServiceAdmin productService) {
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

    private void createIdColumn() {
        id = productGrid.addColumn(product1 -> product1.getId()).setHeader("Id");
    }

    private void createStockStatusColum() {
        stockStatus = productGrid.addColumn(product1 -> product1.getStockStatus().getCount()).setHeader("Stock Status");
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

    private void createDisconutColum() {
        discount = productGrid.addColumn(new ComponentRenderer<>(product -> {
            VerticalLayout hl = new VerticalLayout();
            hl.setAlignItems(FlexComponent.Alignment.START);
            Span span1 = new Span();

            Product product1 = product;
            if (product1.getDiscount() != null) {
                String p = product1.getDiscount().getName() + ". " + product1.getDiscount().getDescription() + ". " + product1.getDiscount().getDiscountType().toString() + ". " + product1.getDiscount().getValue();
                span1.setText(p);
                hl.add(span1);
                return hl;
            } else {
                span1.setText("No disconunt");
                hl.add(span1);
                return hl;
            }

        })).setHeader("Discount");
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
                    BigDecimal p = product1.getPrice().subtract(product1.getDiscount().getValue());
                    span1.setText("New price:" + p.doubleValue() + "P.");
                    span2.setText("Old price:" + product1.getPrice() + "P.");
                } else {
                    BigDecimal p = product1.getPrice().subtract(product1.getPrice().multiply(new BigDecimal(product1.getDiscount().getValue().doubleValue() / 100)));
                    span1.setText("New price:" + p.doubleValue() + "P.");
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

    private Component createSearchBar(ProductServiceAdmin productService) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        searchBar.setPlaceholder("Search...");
        searchBar.setWidth("800px");
        //searchBar.setValueChangeMode(ValueChangeMode.EAGER);
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
        Button newProduct = new Button("New Product");
        newProduct.addClickListener(buttonClickEvent -> {
            try {
                pageNewProduct(productService);
            } catch (JsonProcessingException e) {
                e.getMessage();
            }
        });
        Button discount = new Button("Discount");
        discount.addClickListener(event -> {
            try {
                discountGrid(productService);
            } catch (JsonProcessingException e) {
                e.getMessage();
            }
        });
        Button category = new Button("Category");
        category.addClickListener(event -> {
            try {
                categoryGrid(productService);
            } catch (JsonProcessingException e) {
                e.getMessage();
            }
        });
        newProduct.setWidth("150px");
        newProduct.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        discount.setWidth("100");
        discount.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        category.setWidth("100px");
        category.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        horizontalLayout.add(searchBar, newProduct, discount, category);
        return horizontalLayout;
    }

    private void pageProduct(Product showProduct, ProductServiceAdmin productService) throws JsonProcessingException {
        H3 h3 = new H3("Update product:");
        h3.setSizeFull();
        Dialog dialog = new Dialog();
        dialog.setWidth("600px");

        VerticalLayout verticalLayout1 = new VerticalLayout();
        verticalLayout1.setAlignItems(FlexComponent.Alignment.CENTER);
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        HorizontalLayout horizontalLayout1 = new HorizontalLayout();
        horizontalLayout1.setAlignItems(FlexComponent.Alignment.CENTER);


        TextField name = new TextField("Product name");
        name.setValue(showProduct.getName());
        name.setWidth("550px");
        TextField description = new TextField("Description");
        description.setValue(showProduct.getDescription());
        description.setWidth("550px");
        IntegerField price = new IntegerField("Price");
        price.setValue(showProduct.getPrice().intValue());


        TextField image = new TextField("Image");
        image.setValue(showProduct.getImage());


        IntegerField stockStatus = new IntegerField("Stock Status");
        stockStatus.setValue(showProduct.getStockStatus().getCount());


        ArrayList<Category> categoryArrayList = productService.getCategories(token);
        ComboBox<Category> category = new ComboBox<>("Category");
        category.setItemLabelGenerator(Category::getName);
        category.setItems(categoryArrayList);
        category.setValue(showProduct.getCategory());
        category.addValueChangeListener(categoryClick -> {
            showProduct.setCategory(categoryClick.getValue());
        });

        ArrayList<Discount> discountArrayList = productService.getDiscountList(token);
        ComboBox<Discount> discount = new ComboBox<>("Discount");
        discount.setItemLabelGenerator(Discount::getName);
        discount.setItems(discountArrayList);
        discount.setValue(showProduct.getDiscount());
        discount.addValueChangeListener(discountClick -> {
            showProduct.setDiscount(discountClick.getValue());
        });

        verticalLayout1.add(h3, name, description);
        horizontalLayout.add(price, image, stockStatus);
        horizontalLayout1.add(category, discount);

        Button close = new Button("Cancel", e -> {
            dialog.close();
        });
        Button save = new Button("Save", e -> {
            showProduct.setName(name.getValue());
            showProduct.setDescription(description.getValue());
            BigDecimal newPrice = new BigDecimal(price.getValue());
            showProduct.setPrice(newPrice);
            showProduct.setImage(image.getValue());
            showProduct.setCategory(category.getValue());
            showProduct.setDiscount(discount.getValue());
            StockStatus newStockStatus = new StockStatus(showProduct.getStockStatus().getId(), stockStatus.getValue());

            boolean st = productService.updateStockStatus(newStockStatus, token);
            if (st) {
                showProduct.setStockStatus(newStockStatus);
            }

            boolean b = productService.updateProduct(showProduct, token);
            if (b) {
                createDataProvider(productService);
                productGrid.setDataProvider(dataProvider);
            } else {
                Notification.show("Try again!", 1500, Notification.Position.MIDDLE);
            }
            dialog.close();
        });
        Button detele = new Button("Delete", e -> {
            boolean b = productService.deleteProduct(showProduct, token);
            if (b) {
                createDataProvider(productService);
                productGrid.setDataProvider(dataProvider);
            } else {
                Notification.show("Try again!", 1500, Notification.Position.MIDDLE);
            }
            dialog.close();
        });
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(close, save, detele);
        dialog.add(verticalLayout1, horizontalLayout, horizontalLayout1, buttonLayout);

        dialog.open();
    }

    private void pageNewProduct(ProductServiceAdmin productService) throws JsonProcessingException {
        H3 h3 = new H3("New product:");
        h3.setSizeFull();

        Dialog dialog = new Dialog();
        dialog.setWidth("600px");
        Product newProduct = new Product();
        VerticalLayout verticalLayout1 = new VerticalLayout();
        verticalLayout1.setAlignItems(FlexComponent.Alignment.CENTER);
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        HorizontalLayout horizontalLayout1 = new HorizontalLayout();
        horizontalLayout1.setAlignItems(FlexComponent.Alignment.CENTER);


        TextField name = new TextField("Product name");
        name.setWidth("550px");
        TextField description = new TextField("Description");

        description.setWidth("550px");
        IntegerField price = new IntegerField("Price");

        TextField image = new TextField("Image");
        IntegerField stockStatus = new IntegerField("Stock Status");


        ArrayList<Category> categoryArrayList = productService.getCategories(token);
        ComboBox<Category> category = new ComboBox<>("Category");
        category.setItemLabelGenerator(Category::getName);
        category.setItems(categoryArrayList);

        ArrayList<Discount> discountArrayList = productService.getDiscountList(token);
        ComboBox<Discount> discount = new ComboBox<>("Discount");
        discount.setItemLabelGenerator(Discount::getName);
        discount.setItems(discountArrayList);

        verticalLayout1.add(h3, name, description);
        horizontalLayout.add(price, image, stockStatus);
        horizontalLayout1.add(category, discount);

        Button close = new Button("Cancel", e -> {
            dialog.close();
        });
        Button save = new Button("Save", e -> {
            newProduct.setName(name.getValue());
            newProduct.setDescription(description.getValue());
            BigDecimal newPrice = new BigDecimal(price.getValue());
            newProduct.setPrice(newPrice);
            newProduct.setImage(image.getValue());
            newProduct.setCategory(category.getValue());
            newProduct.setDiscount(discount.getValue());
            StockStatus newStockStatus = new StockStatus();
            newStockStatus.setCount(stockStatus.getValue());
            newProduct.setStockStatus(newStockStatus);

            boolean b = productService.saveProduct(newProduct, token);
            if (b) {
                createDataProvider(productService);
                productGrid.setDataProvider(dataProvider);
            } else {
                Notification.show("Try again!", 1500, Notification.Position.MIDDLE);
            }
            dialog.close();
        });

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(close, save);
        dialog.add(verticalLayout1, horizontalLayout, horizontalLayout1, buttonLayout);

        dialog.open();
    }

    private void createDiscount(ProductServiceAdmin productService) {
        H3 h3 = new H3("New discount:");
        h3.setSizeFull();
        Dialog dialog = new Dialog();
        dialog.setWidth("600px");
        Discount discount = new Discount();

        VerticalLayout verticalLayout1 = new VerticalLayout();
        verticalLayout1.setAlignItems(FlexComponent.Alignment.CENTER);
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        HorizontalLayout horizontalLayout1 = new HorizontalLayout();
        horizontalLayout1.setAlignItems(FlexComponent.Alignment.CENTER);

        TextField name = new TextField("Discount name");
        name.setWidth("550px");
        TextField description = new TextField("Description");
        description.setWidth("550px");
        IntegerField value = new IntegerField("Value");

        DiscountType[] discountTypes = DiscountType.values();
        ComboBox<DiscountType> discountType = new ComboBox<>("Discount type");
        discountType.setItems(discountTypes);
        discountType.addValueChangeListener(discountTypeClick -> {
            discount.setDiscountType(discountTypeClick.getValue());
        });

        verticalLayout1.add(h3, name, description);
        horizontalLayout.add(value, discountType);

        Button close = new Button("Cancel", e -> {
            dialog.close();
        });
        Button save = new Button("Save", e -> {
            discount.setName(name.getValue());
            discount.setDescription(description.getValue());
            BigDecimal newValue = new BigDecimal(value.getValue());
            discount.setValue(newValue);
            discount.setDiscountType(discountType.getValue());
            try {
                boolean b = productService.saveDisconut(discount, token);
                if (b) {
                    ArrayList<Discount> discountNewList = productService.getDiscountList(token);
                    discountGrid.setItems(discountNewList);
                    createDataProvider(productService);
                    productGrid.setDataProvider(dataProvider);
                    dialog.close();
                } else {
                    Notification.show("Try again!", 1500, Notification.Position.MIDDLE);
                }
            } catch (JsonProcessingException ex) {
                ex.getMessage();
            }

        });

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(close, save);
        dialog.add(verticalLayout1, horizontalLayout, buttonLayout);

        dialog.open();
    }

    private void discountGrid(ProductServiceAdmin productService) throws JsonProcessingException {

        ArrayList<Discount> discountArrayList = productService.getDiscountList(token);
        discountGrid.setItems(discountArrayList);
        discountGrid.addColumn(d -> d.getId()).setHeader("Id");
        discountGrid.addColumn(d -> d.getName()).setHeader("Name");
        discountGrid.addColumn(d -> d.getDescription()).setHeader("Description");
        discountGrid.addColumn(d -> d.getValue()).setHeader("Value");
        discountGrid.addColumn(d -> d.getDiscountType()).setHeader("Type");
        discountGrid.addItemClickListener(discountItem -> {
            H3 h3 = new H3("Update discount:");
            h3.setSizeFull();
            Dialog dialog = new Dialog();
            dialog.setWidth("600px");

            VerticalLayout verticalLayout1 = new VerticalLayout();
            verticalLayout1.setAlignItems(FlexComponent.Alignment.CENTER);
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);

            TextField name = new TextField("Discount name");
            name.setWidth("550px");
            name.setValue(discountItem.getItem().getName());
            TextField description = new TextField("Description");
            description.setWidth("550px");
            description.setValue(discountItem.getItem().getDescription());
            IntegerField value = new IntegerField("Value");
            value.setValue(discountItem.getItem().getValue().intValue());

            DiscountType[] discountTypes = DiscountType.values();
            ComboBox<DiscountType> discountType = new ComboBox<>("Discount type");
            discountType.setItems(discountTypes);
            discountType.setValue(discountItem.getItem().getDiscountType());
            discountType.addValueChangeListener(discountTypeClick -> {
                discountItem.getItem().setDiscountType(discountTypeClick.getValue());
            });

            verticalLayout1.add(h3, name, description);
            horizontalLayout.add(value, discountType);

            Button close = new Button("Cancel", e -> {
                dialog.close();
            });
            Button save = new Button("Save", e -> {
                discountItem.getItem().setName(name.getValue());
                discountItem.getItem().setDescription(description.getValue());
                BigDecimal newValue = new BigDecimal(value.getValue());
                discountItem.getItem().setValue(newValue);
                discountItem.getItem().setDiscountType(discountType.getValue());
                try {
                    boolean b = productService.updateDiscount(discountItem.getItem(), token);
                    if (b) {
                        ArrayList<Discount> discountNewList = productService.getDiscountList(token);
                        discountGrid.setItems(discountNewList);
                        createDataProvider(productService);
                        productGrid.setDataProvider(dataProvider);
                        dialog.close();
                    } else {
                        Notification.show("Try again!", 1500, Notification.Position.MIDDLE);
                    }
                } catch (JsonProcessingException ex) {
                    ex.getMessage();
                }
            });
            Button delete = new Button("Delete", e -> {
                try {
                    boolean b = productService.deleteDiscount(discountItem.getItem(), token);
                    if (b) {
                        ArrayList<Discount> discountNewList = productService.getDiscountList(token);
                        discountGrid.setItems(discountNewList);
                        createDataProvider(productService);
                        productGrid.setDataProvider(dataProvider);
                        dialog.close();
                    } else {
                        Notification.show("Try again!", 1500, Notification.Position.MIDDLE);
                    }
                } catch (JsonProcessingException ex) {
                    ex.getMessage();
                }


            });

            save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            HorizontalLayout buttonLayout = new HorizontalLayout();
            buttonLayout.add(close, save, delete);
            dialog.add(verticalLayout1, horizontalLayout, buttonLayout);

            dialog.open();
        });
        Dialog dialog = new Dialog();
        dialog.add(discountGrid);
        Button close = new Button("Close");
        close.addClickListener(buttonClickEvent -> {
            dialog.close();
        });
        Button newDiscount = new Button("New Discount");
        newDiscount.addClickListener(buttonClickEvent -> {
            createDiscount(productService);

        });
        newDiscount.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        dialog.setWidth("800px");
        dialog.add(close, newDiscount);
        dialog.open();
    }

    private void createCategory(ProductServiceAdmin productService) throws JsonProcessingException {
        H3 h3 = new H3("New category:");
        h3.setSizeFull();
        Dialog dialog = new Dialog();
        dialog.setWidth("600px");
        Category category = new Category();

        VerticalLayout verticalLayout1 = new VerticalLayout();
        verticalLayout1.setAlignItems(FlexComponent.Alignment.CENTER);
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        HorizontalLayout horizontalLayout1 = new HorizontalLayout();
        horizontalLayout1.setAlignItems(FlexComponent.Alignment.CENTER);

        TextField name = new TextField("Category name");
        name.setWidth("550px");
        TextField description = new TextField("Description");
        description.setWidth("550px");

        ArrayList<Category> categoryArrayList = productService.getCategories(token);
        ComboBox<Category> categoryComboBox = new ComboBox<>("Category parent");
        categoryComboBox.setItemLabelGenerator(Category::getName);
        categoryComboBox.setItems(categoryArrayList);
        categoryComboBox.addValueChangeListener(discountTypeClick -> {
            category.setCategoryParent(discountTypeClick.getValue());
        });

        verticalLayout1.add(h3, name, description, categoryComboBox);


        Button close = new Button("Cancel", e -> {
            dialog.close();
        });
        Button save = new Button("Save", e -> {
            category.setName(name.getValue());
            category.setDescription(description.getValue());
            category.setCategoryParent(categoryComboBox.getValue());
            try {
                boolean b = productService.saveCategory(category, token);
                if (b) {
                    ArrayList<Category> categoryNewList = productService.getCategories(token);
                    categoryGrid.setItems(categoryNewList);
                    createDataProvider(productService);
                    productGrid.setDataProvider(dataProvider);
                    dialog.close();
                } else {
                    Notification.show("Try again!", 1500, Notification.Position.MIDDLE);
                }
            } catch (JsonProcessingException ex) {
                ex.getMessage();
            }

        });

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(close, save);
        dialog.add(verticalLayout1, horizontalLayout, buttonLayout);

        dialog.open();
    }

    private void categoryGrid(ProductServiceAdmin productService) throws JsonProcessingException {
        ArrayList<Category> categoryArrayList = productService.getCategories(token);
        categoryGrid.setItems(categoryArrayList);
        categoryGrid.addColumn(c -> c.getId()).setHeader("Id");
        categoryGrid.addColumn(c -> c.getName()).setHeader("Name");
        categoryGrid.addColumn(c -> c.getDescription()).setHeader("Description");

        categoryGrid.addColumn(new ComponentRenderer<>(c -> {
            VerticalLayout hl = new VerticalLayout();
            hl.setAlignItems(FlexComponent.Alignment.START);
            Span span1 = new Span();
            Category category = c;
            if (category.getCategoryParent() != null) {
                String parent = category.getCategoryParent().getName();
                span1.setText(parent);
                hl.add(span1);
                return hl;
            } else {
                span1.setText("No parent.");
                hl.add(span1);
                return hl;
            }
        })).setHeader("Category parent");

        categoryGrid.addItemClickListener(categoryItem -> {
            H3 h3 = new H3("Update category:");
            h3.setSizeFull();
            Dialog dialog = new Dialog();
            dialog.setWidth("600px");
            Discount discount = new Discount();

            VerticalLayout verticalLayout1 = new VerticalLayout();
            verticalLayout1.setAlignItems(FlexComponent.Alignment.CENTER);
            HorizontalLayout horizontalLayout = new HorizontalLayout();
            horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
            HorizontalLayout horizontalLayout1 = new HorizontalLayout();
            horizontalLayout1.setAlignItems(FlexComponent.Alignment.CENTER);

            TextField name = new TextField("Category name");
            name.setWidth("550px");
            name.setValue(categoryItem.getItem().getName());
            TextField description = new TextField("Description");
            description.setWidth("550px");
            description.setValue(categoryItem.getItem().getDescription());


            ComboBox<Category> categoryComboBox = new ComboBox<>("Category parent");
            categoryComboBox.setItems(categoryArrayList);
            categoryComboBox.setItemLabelGenerator(Category::getName);
            categoryComboBox.setValue(categoryItem.getItem().getCategoryParent());
            categoryComboBox.addValueChangeListener(changeEvent -> {
                categoryItem.getItem().setCategoryParent(changeEvent.getValue());
            });

            verticalLayout1.add(h3, name, description, categoryComboBox);

            Button close = new Button("Cancel", e -> {
                dialog.close();
            });
            Button save = new Button("Save", e -> {
                categoryItem.getItem().setName(name.getValue());
                categoryItem.getItem().setDescription(description.getValue());
                categoryItem.getItem().setCategoryParent(categoryComboBox.getValue());
                try {
                    boolean b = productService.updateCategory(categoryItem.getItem(), token);
                    if (b) {
                        ArrayList<Category> categories = productService.getCategories(token);
                        categoryGrid.setItems(categories);
                        createDataProvider(productService);
                        productGrid.setDataProvider(dataProvider);
                        dialog.close();
                    } else {
                        Notification.show("Try again!", 1500, Notification.Position.MIDDLE);
                    }
                } catch (JsonProcessingException ex) {
                    ex.getMessage();
                }

            });
            Button delete = new Button("Delete", e -> {
                try {
                    boolean b = productService.deleteCategory(categoryItem.getItem(), token);
                    if (b) {
                        ArrayList<Category> categoryNewList = productService.getCategories(token);
                        categoryGrid.setItems(categoryNewList);
                        createDataProvider(productService);
                        productGrid.setDataProvider(dataProvider);
                        dialog.close();
                    } else {
                        Notification.show("Try again!", 1500, Notification.Position.MIDDLE);
                    }
                } catch (JsonProcessingException ex) {
                    ex.getMessage();
                }

            });

            save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            HorizontalLayout buttonLayout = new HorizontalLayout();
            buttonLayout.add(close, save, delete);
            dialog.add(verticalLayout1, horizontalLayout, buttonLayout);

            dialog.open();
        });
        Dialog dialog = new Dialog();
        dialog.add(categoryGrid);
        Button close = new Button("Close");
        close.addClickListener(buttonClickEvent -> {
            dialog.close();
        });
        Button newDiscount = new Button("New Discount");
        newDiscount.addClickListener(buttonClickEvent -> {
            try {
                createCategory(productService);
            } catch (JsonProcessingException e) {
                e.getMessage();
            }
        });
        newDiscount.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        dialog.setWidth("800px");
        dialog.add(close, newDiscount);
        dialog.open();
    }


    private void addFiltersToGrid(ProductServiceAdmin productService) throws JsonProcessingException {
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
        sort.add("ASC");
        sort.add("DESC");
        priceFilter.setItems(sort);
        priceFilter.setPlaceholder("Sorting");
        priceFilter.setClearButtonVisible(true);
        priceFilter.setWidth("100%");
        priceFilter.addValueChangeListener(
                event -> {
                    if (event.getValue() != null) {
                        if (event.getValue().equals("ASC")) {
                            sortDirection = "ASC";
                            sortBy = "price";
                            currentPage = 0;
                            createDataProvider(productService);
                            productGrid.setDataProvider(dataProvider);
                        } else if (event.getValue().equals("DESC")) {
                            sortDirection = "DESC";
                            sortBy = "price";
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

        ComboBox<String> idFilter = new ComboBox<>();
        ArrayList<String> sort1 = new ArrayList<>();
        sort1.add("ASC");
        sort1.add("DESC");
        idFilter.setItems(sort);
        idFilter.setPlaceholder("Sorting");
        idFilter.setClearButtonVisible(true);
        idFilter.setWidth("100%");
        idFilter.addValueChangeListener(
                event -> {
                    if (event.getValue() != null) {
                        if (event.getValue().equals("ASC")) {
                            sortDirection = "ASC";
                            sortBy = "id";
                            currentPage = 0;
                            createDataProvider(productService);
                            productGrid.setDataProvider(dataProvider);
                        } else if (event.getValue().equals("DESC")) {
                            sortDirection = "DESC";
                            sortBy = "id";
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
        filterRow.getCell(id).setComponent(idFilter);

        ComboBox<String> discountFilter = new ComboBox<>();
        ArrayList<String> sort2 = new ArrayList<>();
        sort2.add("ASC");
        sort2.add("DESC");
        discountFilter.setItems(sort);
        discountFilter.setPlaceholder("Sorting");
        discountFilter.setClearButtonVisible(true);
        discountFilter.setWidth("100%");
        discountFilter.addValueChangeListener(
                event -> {
                    if (event.getValue() != null) {
                        if (event.getValue().equals("ASC")) {
                            sortDirection = "ASC";
                            sortBy = "discount";
                            currentPage = 0;
                            createDataProvider(productService);
                            productGrid.setDataProvider(dataProvider);
                        } else if (event.getValue().equals("DESC")) {
                            sortDirection = "DESC";
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
        filterRow.getCell(discount).setComponent(discountFilter);


    }
}
