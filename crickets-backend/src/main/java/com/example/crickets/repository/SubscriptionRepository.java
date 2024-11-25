package com.example.crickets.repository;

import com.example.crickets.data.*;
import jakarta.enterprise.context.*;

import java.util.*;

import static java.util.stream.Collectors.*;

@ApplicationScoped
public class SubscriptionRepository {

    private final List<Subscription> subscriptions = new ArrayList<>();

    public List<Subscription> findByCreatorName(String creatorName) {
        return subscriptions.stream()
                .filter(sub -> sub.getCreator().getUsername().equals(creatorName))
                .collect(toList());
    }

    public List<Subscription> findBySubscriberName(String subscriberName) {
        return subscriptions.stream()
                .filter(sub -> sub.getSubscriber().getUsername().equals(subscriberName))
                .collect(toList());
    }

    public void save(Subscription subscription) {
        subscriptions.add(subscription);
    }

}
