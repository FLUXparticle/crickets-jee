package com.example.crickets.service;

import com.example.crickets.data.*;
import jakarta.enterprise.context.*;

import java.util.*;
import java.util.concurrent.*;

@ApplicationScoped
public class ChannelMaster {

    // Map von clientId zu PostChannel
    private final Map<String, PostChannel> clients = new ConcurrentHashMap<>();

    public String registerClient(PostChannel channel) {
        // Generiere eine eindeutige clientId mit UUID
        String clientId = UUID.randomUUID().toString();

        clients.put(clientId, channel);

        return clientId;
    }

    public boolean sendToClient(String clientId, Post post) {
        PostChannel channel = clients.get(clientId);
        if (channel != null) {
            if (channel.sendPost(post)) {
                return true;
            }
            clients.remove(clientId);
        }
        return false;
    }

}
