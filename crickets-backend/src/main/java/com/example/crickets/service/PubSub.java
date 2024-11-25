package com.example.crickets.service;

import com.example.crickets.data.*;
import jakarta.enterprise.context.*;

import java.util.*;
import java.util.concurrent.*;

@ApplicationScoped
public class PubSub {

    private final Map<String, List<PostChannel>> channelsMap = new ConcurrentHashMap<>();

    /**
     * Abonniert einen bestimmten Topic und gibt eine BlockingQueue zurück, die verwendet werden kann,
     * um Nachrichten zu diesem Topic zu empfangen.
     *
     * @param topic Der Topic, den der Abonnent erhalten möchte.
     * @param channel Ein Channel, der Post-Nachrichten empfängt.
     */
    public void subscribe(String topic, PostChannel channel) {
        List<PostChannel> channelList = channelsMap.computeIfAbsent(topic, k -> new ArrayList<>());
        channelList.add(channel);
    }

    /**
     * TODO Entfernt die angegebene BlockingQueue aus den Abonnements des Topics.
     *
     * @param topic Der Topic, von dem sich der Abonnent abmelden möchte.
     * @param queue Die zu entfernende BlockingQueue.
     */
    public void unsubscribe(String topic, BlockingQueue<Post> queue) {
/*
        List<BlockingQueue<Post>> channels = channelsMap.get(topic);
        if (channels != null) {
            channels.remove(queue);
            if (channels.isEmpty()) {
                channelsMap.remove(topic);
            }
        }
*/
    }

    /**
     * Veröffentlicht eine Nachricht an alle Abonnenten des Topics.
     *
     * @param topic Der Topic, an den die Nachricht gesendet werden soll.
     * @param post  Die Nachricht, die gesendet wird.
     */
    public void publish(String topic, Post post) {
        List<PostChannel> channels = channelsMap.getOrDefault(topic, Collections.emptyList());
        for (PostChannel channel : channels) {
            channel.sendPost(post);
        }
    }

    /**
     * Aggregiert mehrere BlockingQueues zu einer einzelnen Queue, die Nachrichten aus allen eingehenden Queues sammelt.
     *
     * @param queues Die BlockingQueues, die aggregiert werden sollen.
     * @return Eine BlockingQueue, die alle Nachrichten enthält.
     */
    public static BlockingQueue<Post> aggregate(List<BlockingQueue<Post>> queues) {
        BlockingQueue<Post> aggregatedQueue = new LinkedBlockingQueue<>();

        for (BlockingQueue<Post> queue : queues) {
            new Thread(() -> {
                try {
                    while (true) {
                        Post post = queue.take(); // Blockiert, bis eine Nachricht vorhanden ist.
                        aggregatedQueue.put(post);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Wiederherstellen des unterbrochenen Status.
                }
            }).start();
        }

        return aggregatedQueue;
    }

}
