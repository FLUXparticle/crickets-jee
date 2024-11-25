package com.example.crickets.data;

import java.io.*;

public class User implements Serializable {

    private String username;
    private String server;

    // Konstruktoren
    public User() {
        // empty
    }

    public User(String username) {
        this.username = username;
    }

    public User(String username, String server) {
        this.username = username;
        this.server = server;
    }

    // Getter und Setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

}
