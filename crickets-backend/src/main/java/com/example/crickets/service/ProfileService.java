package com.example.crickets.service;

import com.example.crickets.config.*;
import com.example.crickets.data.*;
import com.example.crickets.repository.*;
import jakarta.enterprise.context.*;
import jakarta.inject.*;

@ApplicationScoped
public class ProfileService {

    @Inject
    private UserRepository userRepository;

    @Inject
    private SubscriptionRepository subscriptionRepository;

    @Inject
    private AppConfig appConfig;

    public int subscriberCount(String creatorName) {
        return subscriptionRepository.findByCreatorName(creatorName).size();
    }

    public String subscribe(String subscriberName, String creatorServer, String creatorName) throws Exception {
        User subscriber = userRepository.findByUsername(subscriberName);

        if (creatorServer == null || creatorServer.isEmpty()) {
            return subscribeLocal(subscriber, creatorName);
        } else {
            // Remote Subscription über REST
            // TODO subscribeRemote(creatorServer, creatorName, subscriber);

            User creator = new User(creatorName, creatorServer);
            subscriptionRepository.save(new Subscription(creator, subscriber));

            return "Successfully subscribed to user '" + creatorName + "' on server '" + creatorServer + "'";
        }
    }

    // Lokale Subscription
    public String subscribeLocal(User subscriber, String creatorName) throws Exception {
        User creator = userRepository.findByUsername(creatorName);
        if (creator == null) {
            throw new Exception("User not found");
        }
        subscriptionRepository.save(new Subscription(creator, subscriber));
        return "Successfully subscribed to user '" + creator.getUsername() + "'";
    }

}
