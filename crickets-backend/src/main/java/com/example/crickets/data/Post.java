package com.example.crickets.data;

import java.io.*;
import java.time.*;

public class Post implements Serializable {

    private long id;
    private User creator;
    private String content;
    private LocalDateTime createdAt;
    private int likes;

    public Post() {
        // empty
    }

    public Post(User creator, String content) {
        this.creator = creator;
        this.content = content;
        createdAt = LocalDateTime.now();
    }

    // Getter und Setter
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

}
