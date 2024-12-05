package com.example.crickets.service;

import com.example.crickets.data.*;
import jakarta.ejb.*;
import jakarta.inject.*;

import java.util.*;

@Stateless
@Remote(TimelineRemoteService.class)
public class TimelineRemoteServiceImpl implements TimelineRemoteService {

    @Inject
    private TimelineService timelineService;

    public List<Post> search(String query) throws Exception {
        System.out.println("Searching ...");
        List<Post> posts = timelineService.searchLocal(query);
        System.out.println("posts = " + posts);
        return posts;
    }

    public void listenToTimeline(String sender, List<String> creatorNames, String clientID) throws Exception {
        System.out.println("Listening to creators: " + creatorNames);
        timelineService.listenToTimelineLocal(creatorNames, post -> {
            return timelineService.sendTimelineUpdateRemote(sender, post, clientID);
        });
    }

    public boolean sendTimelineUpdate(Post post, String channelID) throws Exception {
        return timelineService.sendTimelineUpdateLocal(post, channelID);
    }

    public void likePost(long postId, String creatorName) throws Exception {
        timelineService.likePost(postId, creatorName);
    }

}
