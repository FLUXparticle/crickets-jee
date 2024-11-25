package com.example.crickets.websocket;

import jakarta.json.bind.annotation.*;

public class ChatMessage {

    // JSON-Feldname anpassen
    @JsonbProperty("creator_name")
    private String creatorName;

    private String content;

    @JsonbProperty("created_at")
    private String createdAt;

    public ChatMessage() {
        // Für JSON-B erforderlich
    }

    public ChatMessage(String content) {
        this.content = content;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

}
