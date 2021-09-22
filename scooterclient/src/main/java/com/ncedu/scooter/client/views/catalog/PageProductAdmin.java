package com.ncedu.scooter.client.views.catalog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ncedu.scooter.client.model.product.Category;
import com.ncedu.scooter.client.model.product.Discount;
import com.ncedu.scooter.client.model.product.Product;
import com.ncedu.scooter.client.model.product.StockStatus;

import com.ncedu.scooter.client.service.ProductServiceAdmin;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.H3;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;

import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.internal.MessageDigestUtil;
import com.vaadin.flow.server.StreamResource;

import lombok.AllArgsConstructor;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;

import static com.ncedu.scooter.client.views.catalog.Message.MESSAGE;
@AllArgsConstructor
public class PageProductAdmin {
    private ProductServiceAdmin productService;
    private Product showProduct;
    private String token;
    private Image imageProd;

    public PageProductAdmin(ProductServiceAdmin productService, String token) {
        this.productService = productService;
        this.token = token;
    }

    public Component pageProduct(Grid<Product> productGrid, ListDataProvider<Product> dataProvider) throws JsonProcessingException {
        H3 h3 = new H3("Update product:");
        h3.setSizeFull();
        Dialog dialog = new Dialog();
        dialog.setWidth("750px");
        Product oldProd = new Product();
        oldProd.setImage(showProduct.getImage());
        VerticalLayout verticalLayout1 = new VerticalLayout();
        verticalLayout1.setAlignItems(FlexComponent.Alignment.CENTER);
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        HorizontalLayout horizontalLayout1 = new HorizontalLayout();
        horizontalLayout1.setAlignItems(FlexComponent.Alignment.CENTER);
        VerticalLayout imageLayout = new VerticalLayout();
        imageLayout.setAlignItems(FlexComponent.Alignment.CENTER);


        TextField name = new TextField("Product name");
        name.setValue(showProduct.getName());
        name.setWidth("550px");
        TextField description = new TextField("Description");
        description.setValue(showProduct.getDescription());
        description.setWidth("550px");
        NumberField price = new NumberField("Price");
        price.setValue(showProduct.getPrice().doubleValue());

        String oldImage = showProduct.getImage();

        Image image = new Image("images/"+ oldImage, "Product Info");

        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        VerticalLayout output = new VerticalLayout();

        output.setAlignItems(FlexComponent.Alignment.CENTER);

        upload.addSucceededListener(event -> {
            imageProd = createComponent(event.getMIMEType(),event.getFileName(), buffer.getInputStream());
            showProduct.setImage("("+showProduct.getId()+")"+event.getFileName());
            imageLayout.remove(image);
            output.add(imageProd);

        });

        upload.addFileRejectedListener(event -> {
            Paragraph component = new Paragraph();
            output.removeAll();
            showOutput(event.getErrorMessage(), component, output);
            imageLayout.add(image);

        });
        Product p = new Product();
        Button deleteImage = new Button("Delete image",delete ->{
            p.setImage("logo.png");
            showProduct.setImage("logo.png");
            image.setSrc("images/logo.png");
        });
        upload.getElement().addEventListener("file-remove", event -> {
            if(p.getImage()!= null){
                showProduct.setImage(p.getImage());
            }else {
                showProduct.setImage(oldImage);
            }
            output.removeAll();
            imageLayout.add(image);

        });


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
        stockStatus.setWidth("100px");
        price.setWidth("100px");
        category.setWidth("150px");
        discount.setWidth("150px");
        horizontalLayout.add(stockStatus, price,category,discount);
        horizontalLayout1.add(upload, deleteImage);

        Button close = new Button("Cancel", e -> {
            productGrid.setDataProvider(dataProvider);
            dialog.close();
        });
        Button save = new Button("Save", e -> {

            showProduct.setName(name.getValue());
            showProduct.setDescription(description.getValue());
            BigDecimal newPrice = new BigDecimal(price.getValue());
            showProduct.setPrice(newPrice);
            showProduct.setCategory(category.getValue());
            showProduct.setDiscount(discount.getValue());
            StockStatus newStockStatus = new StockStatus(showProduct.getStockStatus().getId(), stockStatus.getValue());

            boolean st = productService.updateStockStatus(newStockStatus, token);
            if (st) {
                showProduct.setStockStatus(newStockStatus);
            }
            Product b = productService.updateProduct(showProduct, token);
            if (b != null) {
               try {
                   if(ImageIO.read(buffer.getInputStream()) != null && !b.getImage().equals("logo.png")){
                       String nameImage = b.getCategory().getName();
                       File file = new File("src/main/resources/META-INF/resources/images/"+ "("+b.getId()+")_"+ nameImage.toLowerCase().replaceAll(" ","_") + ".png");
                       File fileTarget = new File("target/classes/META-INF/resources/images/"+"("+b.getId()+")_"+ nameImage.toLowerCase().replaceAll(" ","_") + ".png");

                       try {
                           BufferedImage bufferedImage = ImageIO.read(buffer.getInputStream());
                           ImageIO.write(bufferedImage, "png", file);
                           ImageIO.write(bufferedImage, "png", fileTarget);
                       }catch (IOException ex){

                       }
                       b.setImage(file.getName());
                       Product prod = productService.updateProduct(b, token);
                   }

                if(!oldImage.equals("logo.png") && b.getImage().equals("logo.png")){
                       deleteImage(oldImage);
                   }

               }catch (IOException e1){
                   e1.getMessage();
               }
               dialog.close();

                productGrid.setDataProvider(dataProvider);
                productGrid.getDataProvider().refreshAll();
            } else {
                notification(MESSAGE.get("Error. Try again!"), 2000);
            }
            productGrid.setDataProvider(dataProvider);
            productGrid.getDataProvider().refreshAll();
            dialog.close();
        });
        Button detele = new Button("Delete", e -> {
            boolean b = productService.deleteProduct(showProduct, token);
            if (b) {
                if(!showProduct.getImage().equals("logo.png")){
                    deleteImage(showProduct.getImage());
                }
                productGrid.setDataProvider(dataProvider);
               // productGrid.getDataProvider().refreshAll();
            } else {
                notification(MESSAGE.get("Error. Try again"), 2000);
            }
            productGrid.setDataProvider(dataProvider);
            productGrid.getDataProvider().refreshAll();
            dialog.close();
        });
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(close, save, detele);
        imageLayout.add(image);
        verticalLayout1.add(horizontalLayout);
        dialog.add(verticalLayout1, horizontalLayout1,output,imageLayout,buttonLayout);

        dialog.open();
        return dialog;
    }
    public Component pageNewProduct(Grid<Product> productGrid, ListDataProvider<Product> dataProvider) throws JsonProcessingException {
        H3 h3 = new H3("New product:");
        h3.setSizeFull();

        Dialog dialog = new Dialog();
        dialog.setWidth("750px");
        Product newProduct = new Product();
        VerticalLayout verticalLayout1 = new VerticalLayout();
        verticalLayout1.setAlignItems(FlexComponent.Alignment.CENTER);
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        VerticalLayout horizontalLayout1 = new VerticalLayout();
        horizontalLayout1.setAlignItems(FlexComponent.Alignment.CENTER);


        TextField name = new TextField("Product name");
        name.setWidth("550px");
        TextField description = new TextField("Description");

        description.setWidth("550px");
        IntegerField price = new IntegerField("Price");

        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        VerticalLayout output = new VerticalLayout();

        output.setAlignItems(FlexComponent.Alignment.CENTER);

        upload.addSucceededListener(event -> {
            imageProd = createComponent(event.getMIMEType(),event.getFileName(), buffer.getInputStream());
            newProduct.setImage(event.getFileName());
            System.out.println(imageProd.getSrc());
            output.add(imageProd);

        });

        upload.addFileRejectedListener(event -> {
            Paragraph component = new Paragraph();
            output.removeAll();
            showOutput(event.getErrorMessage(), component, output);
            newProduct.setImage("");
        });
        upload.getElement().addEventListener("file-remove", event -> {
            newProduct.setImage("");
            output.removeAll();
        });


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
        stockStatus.setWidth("100px");
        price.setWidth("100px");
        category.setWidth("150px");
        discount.setWidth("150px");
        horizontalLayout.add(stockStatus, price,category,discount);
        horizontalLayout1.add(upload);

        Button close = new Button("Cancel", e -> {
            dialog.close();
        });
        Button save = new Button("Save", e -> {
            if(newProduct.getImage() == null){
                newProduct.setImage("logo.png");
            }
            newProduct.setName(name.getValue());
            newProduct.setDescription(description.getValue());
            BigDecimal newPrice = new BigDecimal(price.getValue());
            newProduct.setPrice(newPrice);
            newProduct.setCategory(category.getValue());
            newProduct.setDiscount(discount.getValue());
            StockStatus newStockStatus = new StockStatus();
            newStockStatus.setCount(stockStatus.getValue());
            newProduct.setStockStatus(newStockStatus);

            Product b = productService.saveProduct(newProduct, token);
            if (b != null) {
                if(!newProduct.getImage().equals("logo.png")){
                    saveImage(newProduct, buffer, b);
                }
                productGrid.setDataProvider(dataProvider);
                productGrid.getDataProvider().refreshAll();
            } else {
                notification(MESSAGE.get("Error.Try again!"), 2000);
            }
            dialog.close();
        });

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(close, save);
        verticalLayout1.add(horizontalLayout );
        dialog.add(verticalLayout1, horizontalLayout1,output,buttonLayout);

        dialog.open();
        return dialog;
    }
    private Notification notification(String message, int time) {
        return Notification.show(message, time, Notification.Position.MIDDLE);
    }
    private void deleteImage(String image){
        File file = new File("src/main/resources/META-INF/resources/images/"+ image);
        File fileTarget = new File("target/classes/META-INF/resources/images/"+ image);
        file.delete();
        fileTarget.delete();
    }
    private Product saveImage(Product newProduct, MemoryBuffer buffer, Product productUpdate){
        String name = newProduct.getCategory().getName();

        File file = new File("src/main/resources/META-INF/resources/images/"+ "("+productUpdate.getId()+")_"+ name.toLowerCase().replaceAll(" ","_") + ".png");
        File fileTarget = new File("target/classes/META-INF/resources/images/"+ "("+productUpdate.getId()+")_"+ name.toLowerCase().replaceAll(" ","_") + ".png");
        try {
            BufferedImage bufferedImage = ImageIO.read(buffer.getInputStream());
            ImageIO.write(bufferedImage, "png", file);
            ImageIO.write(bufferedImage, "png", fileTarget);
        }catch (IOException ex){
            ex.getMessage();
        }

        productUpdate.setImage(file.getName());
        return productService.updateProduct(productUpdate, token);

    }
    private Image createComponent(String mimeType, String fileName, InputStream stream) {
            Image image = new Image();
            try {

                byte[] bytes = IOUtils.toByteArray(stream);
                image.getElement().setAttribute("src", new StreamResource(
                        fileName, () -> new ByteArrayInputStream(bytes)));
                try (ImageInputStream in = ImageIO.createImageInputStream(
                        new ByteArrayInputStream(bytes))) {
                    final Iterator<ImageReader> readers = ImageIO
                            .getImageReaders(in);
                    if (readers.hasNext()) {
                        ImageReader reader = readers.next();
                        try {
                            reader.setInput(in);
                            image.setWidth(reader.getWidth(0) + "px");
                            image.setHeight(reader.getHeight(0) + "px");
                        } finally {
                            reader.dispose();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            image.setSizeFull();
            return image;

    }

    private Component createTextComponent(InputStream stream) {
        String text;
        try {
            text = IOUtils.toString(stream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            text = "exception reading stream";
        }
        return new Text(text);
    }

    private void showOutput(String text, Component content, HasComponents outputContainer) {
        HtmlComponent p = new HtmlComponent(Tag.P);
        p.getElement().setText(text);
        outputContainer.add(p);
        outputContainer.add(content);
    }
}
