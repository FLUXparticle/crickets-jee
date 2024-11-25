package com.example.crickets.resource;

import com.example.crickets.data.*;
import com.example.crickets.service.*;
import jakarta.inject.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.security.*;
import java.util.*;

import static java.util.Collections.*;

@Path("/api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TimelineResource {

    @Inject
    private TimelineService timelineService;

    @GET
    @Path("/search")
    public Response search(@QueryParam("s") String server, @QueryParam("q") String query) {
        try {
            List<Post> results = timelineService.search(server, query);
            if (!results.isEmpty()) {
                return Response.ok(singletonMap("searchResults", results)).build();
            } else {
                return Response.ok(singletonMap("error", "nothing found")).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.ok(singletonMap("error", e.getMessage())).build();
        }
    }

    @POST
    @Path("/post")
    public Response post(PostRequest request, @Context SecurityContext securityContext) {
        try {
            Principal principal = securityContext.getUserPrincipal();
            String creatorName = principal.getName();
            timelineService.post(creatorName, request.getContent());
            return Response.ok().build();
        } catch (Exception e) {
            return Response.ok(singletonMap("error", e.getMessage())).build();
        }
    }

    @POST
    @Path("/like")
    public Response likePost(LikePostRequest request, @Context SecurityContext securityContext) {
        try {
            User user = (User) securityContext.getUserPrincipal();
            timelineService.likePost(request.getPostId(), request.getCreatorName());
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(singletonMap("error", e.getMessage()))
                .build();
        }
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
