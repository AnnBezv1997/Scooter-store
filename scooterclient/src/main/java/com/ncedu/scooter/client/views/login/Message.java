package com.ncedu.scooter.client.views.login;

import java.util.HashMap;

public final class Message {
    static final HashMap<String, String> MESSAGE = new HashMap<>();

    static {
        MESSAGE.put("Error", "Sorry:( Try again or registration.");
        MESSAGE.put("Password", "Password. At least 6 characters:");
    }
}
