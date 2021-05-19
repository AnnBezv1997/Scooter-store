package com.ncedu.scooter.client.views.user;

import java.util.HashMap;

public final class Message {
    static final HashMap<String, String> MESSAGE = new HashMap<>();

    static {

        MESSAGE.put("Error", "An error occurred.Try again.");
        MESSAGE.put("logout", "Are you sure you want to get out?");

    }
}
