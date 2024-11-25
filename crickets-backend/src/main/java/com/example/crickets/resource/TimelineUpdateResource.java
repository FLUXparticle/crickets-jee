package com.example.crickets.resource;

import com.example.crickets.service.*;
import jakarta.inject.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.sse.*;

@Path("/api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.SERVER_SENT_EVENTS)
public class TimelineUpdateResource {

    @Inject
    private TimelineService timelineService;

    @Context
    private Sse sse;

    @GET
    @Path("/timeline")
    public void timeline(@Context SecurityContext securityContext, @Context SseEventSink sseEventSink) {
        String subscriberName = securityContext.getUserPrincipal().getName();

        timelineService.receiveTimelineUpdates(subscriberName, post -> {
            System.out.println("post = " + post);
            if (sseEventSink.isClosed()) {
                return false;
            }

            OutboundSseEvent event = sse.newEventBuilder()
                    .mediaType(MediaType.APPLICATION_JSON_TYPE)
                    .data(post)
                    .build();
            sseEventSink.send(event);

            return true;
        });
    }

    // DTOs
    public static class PostRequest {
        private String content;
        // Getter und Setter
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }

    public static class LikePostRequest {
        private long postId;
        private String creatorName;
        // Getter und Setter
        public long getPostId() { return postId; }
        public void setPostId(long postId) { this.postId = postId; }
        public String getCreatorName() { return creatorName; }
        public void setCreatorName(String creatorName) { this.creatorName = creatorName; }
    }

}
