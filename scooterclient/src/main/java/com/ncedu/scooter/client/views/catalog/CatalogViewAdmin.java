package com.ncedu.scooter.client.views.catalog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ncedu.scooter.client.model.product.*;
import com.ncedu.scooter.client.model.request.product.ProductRequest;
import com.ncedu.scooter.client.model.request.product.ProductResponse;
import com.ncedu.scooter.client.model.request.user.AuthResponse;
import com.ncedu.scooter.client.model.user.User;
import com.ncedu.scooter.client.service.OrderService;
import com.ncedu.scooter.client.service.ProductService;
import com.ncedu.scooter.client.service.ProductServiceAdmin;
import com.ncedu.scooter.client.views.main.ViewCatalog;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import java.util.Comparator;

import static com.ncedu.scooter.client.views.catalog.Message.MESSAGE;

@Route(value = "admin", layout = ViewCatalog.class)
@PageTitle("Admin")
@CssImport("./views/catalog/catalog-view-admin.css")
public class CatalogViewAdmin extends Div {
    private AuthResponse authResponse = (AuthResponse) VaadinSession.getCurrent().getAttribute("authResponse");
    private String token = (String) VaadinSession.getCurrent().getAttribute("token");
    private User user;


    private Grid<Product> productGrid = new Grid<>(Product.class);
    private Grid<Discount> discountGrid = new Grid<>();
    private Grid<Category> categoryGrid = new Grid<>();

    private ListDataProvider<Product> dataProvider;
    private ListDataProvider<Category> dataProviderCategory;
    private ListDataProvider<Discount> dataProviderDiscount;

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

    public CatalogViewAdmin(ProductServiceAdmin productService, ProductService service, OrderService orderService) throws JsonProcessingException {
        CatalogView catalogView = new CatalogView(service, orderService);
        if (authResponse == null) {
            catalogView.errorPage();
            UI.getCurrent().navigate(ErrorView.class);

        } else if (!authResponse.getUser().getRole().getName().equals("ROLE_ADMIN")) {
           catalogView.errorPage();
            UI.getCurrent().navigate(ErrorView.class);
        } else if (authResponse.getUser().getRole().getName().equals("ROLE_ADMIN")){
            user = authResponse.getUser();
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

            createDataProviderCategory(productService);
            createGridCategory();
            createColumnCategoryGrid();

            createDataProviderDiscount(productService);
            createGridDiscountt();
            createColumnDiscountGrid();

            Button next = new Button(MESSAGE.get("Next"), buttonClickEvent -> {
                if (currentPage < totalPages - 1) {
                    currentPage++;
                    createDataProvider(productService);
                    productGrid.setDataProvider(dataProvider);
                } else {
                    notification(MESSAGE.get("The end"), 1200);

                }
            });
            Button back = new Button(MESSAGE.get("Back"), buttonClickEvent -> {
                if (currentPage > 0 && currentPage < totalPages) {
                    currentPage--;
                    createDataProvider(productService);
                    productGrid.setDataProvider(dataProvider);
                } else {
                    notification(MESSAGE.get("Go"), 1000);
                }
            });
            add(back);
            add(next);
            productGrid.addItemClickListener(event -> {
                Product showProduct = event.getItem();
                try {
                    pageProduct(showProduct, productService);
                    createDataProvider(productService);
                    productGrid.setDataProvider(dataProvider);
                    productGrid.getDataProvider().refreshAll();
                } catch (JsonProcessingException e) {
                    e.getMessage();
                }


            });
        }


    }
    private Notification notification(String message, int time) {
        return Notification.show(message, time, Notification.Position.MIDDLE);
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
            Image logo = new Image("images/" + product.getImage(), "ScooterClient logo");
            logo.setHeight("150px");
            logo.setHeight("150px");
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
        category = productGrid.addColumn(new ComponentRenderer<>(product1 -> {
            VerticalLayout hl = new VerticalLayout();
            hl.setAlignItems(FlexComponent.Alignment.START);
            Span span1 = new Span();
            // Product product1 = product;
            if (product1.getCategory() == null) {
                span1.setText("");
                hl.add(span1);
                return hl;
            } else {
                span1.setText(product1.getCategory().getName());
                hl.add(span1);
                return hl;
            }
        })).setHeader("Category");

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
                    BigDecimal price = p.getPrice().subtract(new BigDecimal(totalDiscount)).setScale(1,BigDecimal.ROUND_HALF_UP);;
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

        Dialog dialogDiscount = new Dialog();
        try {
            discountGrid(productService, dialogDiscount, new Dialog());
        } catch (JsonProcessingException e) {
            e.getMessage();
        }
        Button discount = new Button("Discount");
        discount.addClickListener(event -> {
            dialogDiscount.open();
        });


        Dialog dialogCategory = new Dialog();
        try {
            categoryGrid(productService, dialogCategory, new Dialog());
        } catch (JsonProcessingException e) {
            e.getMessage();
        }
        Button category = new Button("Category", event -> {
            dialogCategory.open();
        });

        newProduct.setWidth("150px");
        newProduct.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        discount.setWidth("150px");
        discount.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        category.setWidth("150px");
        category.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        horizontalLayout.add(searchBar, newProduct, discount, category);
        return horizontalLayout;
    }


    private void pageProduct(Product showProduct, ProductServiceAdmin productService) throws JsonProcessingException {
       PageProductAdmin pageProductAdmin = new PageProductAdmin(productService, showProduct, token, new Image());
       pageProductAdmin.pageProduct(productGrid, dataProvider);
       createDataProvider(productService);
       productGrid.setDataProvider(dataProvider);


    }

    private void pageNewProduct(ProductServiceAdmin productService) throws JsonProcessingException {
        PageProductAdmin pageProductAdmin = new PageProductAdmin(productService,token);
        pageProductAdmin.pageNewProduct(productGrid, dataProvider);
        createDataProvider(productService);
        productGrid.setDataProvider(dataProvider);
    }


    private void createDataProviderDiscount(ProductServiceAdmin productService) throws JsonProcessingException {

        ArrayList<Discount> discountArrayList = productService.getDiscountList(token);
        discountArrayList.sort(new Comparator<Discount>() {
            public int compare(Discount d1, Discount d2) {
                return d1.getId().compareTo(d2.getId());
            }
        });
        dataProviderDiscount = new ListDataProvider<>(discountArrayList);
    }

    private Component createGridDiscountt() {
        discountGrid.getDataProvider().refreshAll();
        discountGrid.setDataProvider(dataProviderDiscount);
        discountGrid.removeAllColumns();
        return discountGrid;
    }

    private void createColumnDiscountGrid() {
        discountGrid.addColumn(d -> d.getId()).setHeader("Id");
        discountGrid.addColumn(d -> d.getName()).setHeader("Name");
        discountGrid.addColumn(d -> d.getDescription()).setHeader("Description");
        discountGrid.addColumn(d -> d.getValue()).setHeader("Value");
        discountGrid.addColumn(d -> d.getDiscountType()).setHeader("Type");
    }

    private void discountGrid(ProductServiceAdmin productService, Dialog dialog, Dialog updateDiscount) throws JsonProcessingException {
        dialog.add(discountGrid);
        discountGrid.addItemClickListener(discountItem -> {
            updateDiscount.removeAll();
            updateDiscount(discountItem.getItem(), productService, updateDiscount);
            updateDiscount.open();
        });

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
    }
    private void createDiscount(ProductServiceAdmin productService) {
      CreateDiscount createDicount = new CreateDiscount(productService, token);
      createDicount.createDiscount(productGrid,discountGrid,dataProvider);
    }

    private void updateDiscount(Discount discountItem, ProductServiceAdmin productService, Dialog dialog) {
        UpdateDiscount updateDiscount = new UpdateDiscount(discountItem, productService, dialog, token);
        updateDiscount.updateDiscount(productGrid,discountGrid,dataProvider);
    }

    private void createCategory(ProductServiceAdmin productService) throws JsonProcessingException{
       CreateCategory createCategory = new CreateCategory(productService, token);
       createCategory.createCategory(productGrid,categoryGrid,dataProvider);
    }

    private void createDataProviderCategory(ProductServiceAdmin productService) throws JsonProcessingException {

        ArrayList<Category> categoryArrayList = productService.getCategories(token);
        categoryArrayList.sort(new Comparator<Category>() {
            public int compare(Category c1, Category c2) {
                return c1.getId().compareTo(c2.getId());
            }
        });
        dataProviderCategory = new ListDataProvider<>(categoryArrayList);
    }

    private Component createGridCategory() {
        categoryGrid.getDataProvider().refreshAll();
        categoryGrid.setDataProvider(dataProviderCategory);
        categoryGrid.removeAllColumns();
        return categoryGrid;
    }

    private void categoryGrid(ProductServiceAdmin productService, Dialog dialog, Dialog dialogCategory) throws JsonProcessingException {
        dialog.add(categoryGrid);
        dialog.setWidth("800px");
        categoryGrid.addItemClickListener(categoryItem -> {
            dialogCategory.removeAll();
            updateCategory(categoryItem.getItem(), productService, dialogCategory);
            dialogCategory.open();
        });

        Button close = new Button("Close");
        close.addClickListener(buttonClickEvent -> {
            dialog.close();
        });
        Button newCategory = new Button("New Category");
        newCategory.addClickListener(buttonClickEvent -> {
            try {
                createCategory(productService);
            } catch (JsonProcessingException e) {
                e.getMessage();
            }
        });
        newCategory.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        dialog.add(close, newCategory);

    }

    private void createColumnCategoryGrid() {
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
    }

    private boolean parent(Category category1, Category category2) {
        ArrayList<Category> childCategory = new ArrayList<>();
        if (category1.equals(category2.getCategoryParent())) {
            return true;
        } else {
            if (category2.getCategoryParent() != null) {
                childCategory.add(category2.getCategoryParent());
                child(childCategory, category2.getCategoryParent());
            }
            if (category1.getCategoryParent() != null) {
                childCategory.add(category1.getCategoryParent());
                child(childCategory, category1.getCategoryParent());
            }
            for (Category c : childCategory) {
                if (c.equals(category1) || c.equals(category2)) {
                    return true;
                }
            }
            return false;
        }
    }

    private void child(ArrayList<Category> childCategory, Category category1) {
        if (category1.getCategoryParent() != null) {
            childCategory.add(category1.getCategoryParent());
            child(childCategory, category1.getCategoryParent());
        }
    }

    private void updateCategory(Category categoryItem, ProductServiceAdmin productService, Dialog dialog1) {

        H3 h3 = new H3("Update category:");
        h3.setSizeFull();

        dialog1.setWidth("600px");

        VerticalLayout verticalLayout1 = new VerticalLayout();
        verticalLayout1.setAlignItems(FlexComponent.Alignment.CENTER);

        TextField name = new TextField("Category name");
        name.setWidth("550px");
        name.setValue(categoryItem.getName());
        TextField description = new TextField("Description");
        description.setWidth("550px");
        description.setValue(categoryItem.getDescription());

        ComboBox<Category> categoryComboBox = new ComboBox<>("Category parent");
        try {
            ArrayList<Category> categories = productService.getCategories(token);
            Category categoryNoParent = new Category();
            categoryNoParent.setName("No parent");
            categories.add(categoryNoParent);
            categories.remove(categoryItem);
            categoryComboBox.setItems(categories);
            categoryComboBox.setItemLabelGenerator(Category::getName);
            categoryComboBox.setValue(categoryItem.getCategoryParent());
            categoryComboBox.addValueChangeListener(changeEvent -> {
            });
            verticalLayout1.add(h3, name, description, categoryComboBox);
        } catch (JsonProcessingException e) {
            e.getMessage();
        }


        Button save = new Button("Save", e -> {
            Category newParent = categoryComboBox.getValue();
            categoryItem.setDescription(description.getValue());
            categoryItem.setName(name.getValue());
            try {
                if (newParent != null) {
                    if (newParent.getName().equals("No parent")) {
                        categoryItem.setCategoryParent(null);
                    } else {
                        if (parent(categoryItem, newParent)) {
                            notification(MESSAGE.get("ErrorParent"), 2500);

                        } else {
                            categoryItem.setCategoryParent(newParent);
                        }
                    }
                }

                boolean b = productService.updateCategory(categoryItem, token);
                if (b) {
                    createDataProviderCategory(productService);
                    categoryGrid.setDataProvider(dataProviderCategory);

                } else {
                    notification(MESSAGE.get("Error"), 2500);
                }
            } catch (JsonProcessingException ex) {
                ex.getMessage();
            }

        });
        Button delete = new Button("Delete", e -> {
            try {
                boolean b = productService.deleteCategory(categoryItem, token);
                if (b) {
                    createDataProviderCategory(productService);
                    categoryGrid.setDataProvider(dataProviderCategory);
                    createDataProvider(productService);
                    productGrid.setDataProvider(dataProvider);
                    dialog1.close();
                } else {
                    notification(MESSAGE.get("Try"), 2500);
                }
            } catch (JsonProcessingException ex) {
                ex.getMessage();
            }
        });

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout buttonLayout = new HorizontalLayout();
        Button close = new Button("Cancel", e -> {
            dialog1.close();
        });
        buttonLayout.add(close, save, delete);
        dialog1.add(verticalLayout1, buttonLayout);

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
        sort.add(MESSAGE.get("Asc"));
        sort.add(MESSAGE.get("Desc"));
        priceFilter.setItems(sort);
        priceFilter.setPlaceholder("Sorting");
        priceFilter.setClearButtonVisible(true);
        priceFilter.setWidth("100%");
        priceFilter.addValueChangeListener(
                event -> {
                    if (event.getValue() != null) {
                        if (event.getValue().equals(MESSAGE.get("Asc"))){
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
        sort1.add(MESSAGE.get("Asc"));
        sort1.add(MESSAGE.get("Desc"));
        idFilter.setItems(sort);
        idFilter.setPlaceholder("Sorting");
        idFilter.setClearButtonVisible(true);
        idFilter.setWidth("100%");
        idFilter.addValueChangeListener(
                event -> {
                    if (event.getValue() != null) {
                        if (event.getValue().equals("Ascending")) {
                            sortDirection = "ASC";
                            sortBy = "id";
                            currentPage = 0;
                            createDataProvider(productService);
                            productGrid.setDataProvider(dataProvider);
                        } else if (event.getValue().equals("Descending")) {
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
        sort2.add(MESSAGE.get("Asc"));
        sort2.add(MESSAGE.get("Desc"));
        discountFilter.setItems(sort);
        discountFilter.setPlaceholder("Sorting");
        discountFilter.setClearButtonVisible(true);
        discountFilter.setWidth("100%");
        discountFilter.addValueChangeListener(
                event -> {
                    if (event.getValue() != null) {
                        if (event.getValue().equals("Ascending")) {
                            sortDirection = "ASC";
                            sortBy = "discount";
                            currentPage = 0;
                            createDataProvider(productService);
                            productGrid.setDataProvider(dataProvider);
                        } else if (event.getValue().equals("Descending")) {
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
