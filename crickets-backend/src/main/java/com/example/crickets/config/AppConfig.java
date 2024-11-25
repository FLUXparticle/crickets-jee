package com.example.crickets.config;

import jakarta.enterprise.context.*;
import jakarta.inject.*;

@ApplicationScoped
@Named("appConfig")
public class AppConfig {

    private String hostname;
    private String apiKey;

    public AppConfig() {
        // Werte aus Umgebungsvariablen oder Konfigurationsdateien laden
        this.hostname = System.getenv("HOSTNAME") != null ? System.getenv("HOSTNAME") : "localhost";
        this.apiKey = System.getenv("API_KEY") != null ? System.getenv("API_KEY") : "your-api-key";
    }

    public String getHostname() {
        return hostname;
    }

    public String getApiKey() {
        return apiKey;
    }

}
