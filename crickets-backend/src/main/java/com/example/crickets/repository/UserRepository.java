package com.example.crickets.repository;

import com.example.crickets.data.*;
import jakarta.enterprise.context.*;

import java.util.*;
import java.util.concurrent.*;

@ApplicationScoped
public class UserRepository {

    private final Map<String, User> users = new ConcurrentHashMap<>();

    public UserRepository() {
        // Benutzer hinzufügen
        save(new User("admin"));
        save(new User("manager"));
        save(new User("helpdesk"));
        save(new User("employee"));
    }

    public User findByUsername(String username) {
        return users.get(username);
    }

    public void save(User user) {
        users.put(user.getUsername(), user);
    }

}
