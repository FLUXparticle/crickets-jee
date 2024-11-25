package com.example.crickets.data;

public class Subscription {

    private User creator;
    private User subscriber;

    // Konstruktoren
    public Subscription() {}

    public Subscription(User creator, User subscriber) {
        this.creator = creator;
        this.subscriber = subscriber;
    }

    // Getter und Setter
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public User getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(User subscriber) {
        this.subscriber = subscriber;
    }

}
