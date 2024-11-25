package com.example.crickets.deployment;

import java.nio.file.*;
import java.util.*;

public class MultiServerLauncher {

    public static void main(String[] args) throws Exception {
        // Konfigurationen für die Server
        Map<String, Integer> serverConfigs = new HashMap<>();
        serverConfigs.put("Server1", 8081);
        serverConfigs.put("Server2", 8082);
        serverConfigs.put("Server3", 8083);

        for (Map.Entry<String, Integer> entry : serverConfigs.entrySet()) {
            String serverName = entry.getKey();
            int port = entry.getValue();

            // Erstelle und starte den Server
            Thread serverThread = new Thread(() -> {
                try {
                    startServer(serverName, port);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            serverThread.setName(serverName + "-Thread");
            serverThread.start();
            System.out.println(serverName + " gestartet auf Port " + port);
        }
    }

    private static void startServer(String serverName, int port) throws Exception {
        Path directory = Paths.get("crickets-deployment");

        // Pfad zu deinem Bootable JAR
        Path jarPath = directory.resolve("target/crickets-deployment-1.0-SNAPSHOT-bootable.jar");

        ProcessBuilder processBuilder = new ProcessBuilder(
                "java", "-jar",
                jarPath.toAbsolutePath().toString(),
                "-Djboss.http.port=" + port
        );

        processBuilder.directory(directory.toFile()); // Arbeitsverzeichnis
        // Konsolenausgabe anpassen, um den Server-Namen hinzuzufügen
        processBuilder.redirectErrorStream(true); // Fehler und Standardausgabe zusammenführen

        Process process = processBuilder.start();

        // Ausgabe des Prozesses mit Servernamen versehen
        try (var reader = new Scanner(process.getInputStream())) {
            while (reader.hasNextLine()) {
                System.out.println("[" + serverName + "] " + reader.nextLine());
            }
        }

        // Warte, bis der Server beendet wird
        process.waitFor();
    }

}
