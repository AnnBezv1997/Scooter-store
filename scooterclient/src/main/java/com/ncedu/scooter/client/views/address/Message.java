package com.ncedu.scooter.client.views.address;

import java.util.HashMap;

public final class Message {
    static final HashMap<String, String> MESSAGE = new HashMap<>();

    static {
        MESSAGE.put("Cancel", "Cancel");
        MESSAGE.put("Delete", "Delete");
        MESSAGE.put("Сonfi", "Are you sure?");
        MESSAGE.put("No", "No");
        MESSAGE.put("Yes, detele", "Yes, detele");
        MESSAGE.put("Done", "Done!");
        MESSAGE.put("Error", "An error occurred.Try again.");
        MESSAGE.put("Save", "Save");
        MESSAGE.put("Addresses", "Addresses");
        MESSAGE.put("New", "New addresses");
    }
}
