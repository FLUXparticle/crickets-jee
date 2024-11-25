package com.example.crickets.config;

import jakarta.enterprise.context.*;

@ApplicationScoped
public class ServerConfig {

    private int port;

    public ServerConfig() {
        String portStr = System.getProperty("jboss.http.port");
        this.port = portStr != null ? Integer.parseInt(portStr) : 8080;
    }

    public int getPort() {
        return port;
    }

}
