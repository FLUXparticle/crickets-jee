package com.example.crickets.resource;

import com.example.crickets.service.*;
import jakarta.inject.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import static java.util.Collections.*;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProfileResource {

    @Inject
    private ProfileService profileService;

    @Inject
    private UserResource userResource;

    @GET
    @Path("/profile")
    public Response getProfile() {
        try {
            String username = userResource.getUsername();
            int subscriberCount = profileService.subscriberCount(username);
            return Response.ok(singletonMap("subscriberCount", subscriberCount)).build();
        } catch (Exception e) {
            return Response.ok(singletonMap("error", e.getMessage())).build();
        }
    }

    @POST
    @Path("/subscribe")
    public Response subscribe(SubscribeRequest request) {
        try {
            String subscriberName = userResource.getUsername();
            String message = profileService.subscribe(subscriberName, request.getServer(), request.getCreatorName());
            return Response.ok(singletonMap("success", message)).build();
        } catch (Exception e) {
            return Response.ok(singletonMap("error", e.getMessage())).build();
        }
    }

    // SubscribeRequest DTO
    public static class SubscribeRequest {

        private String server;
        private String creatorName;

        // Getter und Setter
        public String getServer() {
            return server;
        }

        public void setServer(String server) {
            this.server = server;
        }

        public String getCreatorName() {
            return creatorName;
        }

        public void setCreatorName(String creatorName) {
            this.creatorName = creatorName;
        }
    }

}
