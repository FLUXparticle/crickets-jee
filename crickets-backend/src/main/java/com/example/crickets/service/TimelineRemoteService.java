package com.example.crickets.service;

import com.example.crickets.data.*;

import java.util.*;

public interface TimelineRemoteService {
    List<Post> search(String query) throws Exception;
    void listenToTimeline(String sender, List<String> creatorNames, String clientID) throws Exception;
    boolean sendTimelineUpdate(Post post, String channelID) throws Exception;
    void likePost(long postId, String creatorName) throws Exception;
}
