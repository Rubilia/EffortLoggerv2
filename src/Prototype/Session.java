package Prototype;

import java.io.*;

public class Session {
    private static Session instance = new Session();
    private String username;

    private Session() {}

    public static Session getInstance() {
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Method to log out the user
    public void logout() {
        username = null;
    }
}