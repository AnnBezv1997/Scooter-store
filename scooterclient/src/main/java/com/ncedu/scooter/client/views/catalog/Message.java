package com.ncedu.scooter.client.views.catalog;

import java.util.HashMap;

public final class Message {
    static final HashMap<String, String> MESSAGE = new HashMap<>();

    static {
        MESSAGE.put("Cancel", "Cancel");
        MESSAGE.put("Delete", "Delete");
        MESSAGE.put("Сonfi", "Are you sure?");
        MESSAGE.put("No", "No");
        MESSAGE.put("Go", "Beginning!");
        MESSAGE.put("The end", "The end!");
        MESSAGE.put("Back", "Back");
        MESSAGE.put("Next", "Next");
        MESSAGE.put("Category", "Category");
        MESSAGE.put("Description", "Description");
        MESSAGE.put("Product", "Product");
        MESSAGE.put("Try", "Try again!");
        MESSAGE.put("In stock","In stock");
        MESSAGE.put("Out of stock","Out of stock");
        MESSAGE.put("Add","Add to basket");
        MESSAGE.put("AddDone","Product added, go to the shopping cart!");
        MESSAGE.put("Asc","Ascending");
        MESSAGE.put("Desc","Descending");
        MESSAGE.put("FirstDiscount","First with discount");
        MESSAGE.put("ErrorParent","No changes have been made. Invalid parent.");
        MESSAGE.put("Error","No changes have been made. Check the data and try again.");

        MESSAGE.put("Price", "Price : ");
        MESSAGE.put("New price", "New price:");
        MESSAGE.put("Old price", "Old price:");



    }
}
