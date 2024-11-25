package com.example.crickets.repository;

import com.example.crickets.data.*;
import jakarta.enterprise.context.*;

import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;

@ApplicationScoped
public class PostRepository {
    private final List<Post> posts = new ArrayList<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public Post findById(long id) {
        return posts.stream()
                .filter(post -> post.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public void save(Post post) {
        post.setId(nextId.getAndIncrement());
        posts.add(post);
    }

    public List<Post> findByContentContains(String query) {
        return posts.stream()
                .filter(post -> post.getContent().contains(query))
                .collect(Collectors.toList());
    }
}