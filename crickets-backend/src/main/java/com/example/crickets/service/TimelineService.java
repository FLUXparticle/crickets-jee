package com.example.crickets.service;

import com.example.crickets.config.*;
import com.example.crickets.data.*;
import com.example.crickets.repository.*;
import jakarta.enterprise.context.*;
import jakarta.inject.*;

import java.util.*;
import java.util.Map.*;

import static java.util.Collections.*;
import static java.util.stream.Collectors.*;

@ApplicationScoped
public class TimelineService {

    @Inject
    private UserRepository userRepository;

    @Inject
    private PostRepository postRepository;

    @Inject
    private SubscriptionRepository subscriptionRepository;

    @Inject
    private ChannelMaster channelMaster;

    @Inject
    private PubSub pubSub;

    @Inject
    private ServerConfig serverConfig;

    /**
     * Liefert Updates für einen Abonnenten, indem Posts von lokalen und entfernten Servern gesammelt werden.
     *
     * @param subscriberName Name des Abonnenten.
     */
    public void receiveTimelineUpdates(String subscriberName, PostChannel postChannel) {
        // Erstmal die eigenen Posts abonnieren
        pubSub.subscribe(subscriberName, postChannel);

        String clientID = null;

        // Sammeln von Abonnements
        List<Subscription> subscriptions = subscriptionRepository.findBySubscriberName(subscriberName);
        Map<String, List<String>> groups = subscriptions.stream()
                .collect(groupingBy(
                        sub -> sub.getCreator().getServer(),
                        mapping(
                                sub -> sub.getCreator().getUsername(),
                                toList()
                        )
                ));

        for (Entry<String, List<String>> entry : groups.entrySet()) {
            String server = entry.getKey();
            List<String> creatorNames = entry.getValue();
            if (server.isEmpty()) {
                System.out.println("Listening to " + creatorNames + " ...");
                listenToTimelineLocal(creatorNames, postChannel);
            } else {
                System.out.println("Listening to " + creatorNames + " on " + server + " ...");
                if (clientID == null) {
                    clientID = channelMaster.registerClient(postChannel);
                }
                // TODO listenToTimelineRemote(server, creatorNames, clientID);
            }
        }
    }

    public void listenToTimelineLocal(List<String> creatorNames, PostChannel postChannel) {
        for (String creatorName : creatorNames) {
            pubSub.subscribe(creatorName, postChannel);
        }
    }

    public boolean sendTimelineUpdateLocal(Post post, String channelID) {
        return channelMaster.sendToClient(channelID, post);
    }

    /**
     * Fügt einen neuen Post hinzu und benachrichtigt Abonnenten.
     *
     * @param creatorName Der Ersteller des Posts.
     * @param content Der Inhalt des Posts.
     * @throws Exception Falls ein Fehler auftritt.
     */
    public void post(String creatorName, String content) throws Exception {
        User creator = userRepository.findByUsername(creatorName);

        Post post = new Post(creator, content);
        postRepository.save(post);

        pubSub.publish(creator.getUsername(), post);
    }

    /**
     * Sucht nach Posts mit einem bestimmten Inhalt auf lokalen oder entfernten Servern.
     *
     * @param server Remote-Server-Adresse (leer für lokale Suche).
     * @param query  Suchabfrage.
     * @return Liste der gefundenen Posts.
     */
    public List<Post> search(String server, String query) throws Exception {
        if (server == null || server.isEmpty()) {
            return searchLocal(query);
        }

        return emptyList();
    }

    public List<Post> searchLocal(String query) {
        return postRepository.findByContentContains(query);
    }

    /**
     * Fügt einem Post ein Like hinzu.
     *
     * @param postID     Die ID des Posts.
     * @param creatorName Der Ersteller des Posts im Format "username@server".
     * @throws Exception Falls ein Fehler auftritt.
     */
    public void likePost(long postID, String creatorName) throws Exception {
        String[] parts = creatorName.split("@");
        String server = parts.length > 1 ? parts[1] : "";
        String username = parts[0];

        if (server.isEmpty()) {
            // Lokaler Post
            Post post = postRepository.findById(postID);
            if (post != null) {
                post.setLikes(post.getLikes() + 1);
                pubSub.publish(post.getCreator().getUsername(), post);
            } else {
                throw new Exception("Post nicht gefunden");
            }
        } else {
            // TODO Remote Post
            // TimelineServiceRemote client = getRemoteClient(server);
            // client.likePost(postID, username);
        }
    }

}
