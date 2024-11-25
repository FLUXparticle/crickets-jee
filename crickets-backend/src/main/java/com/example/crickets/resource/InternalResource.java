package com.example.crickets.resource;

import com.example.crickets.data.*;
import com.example.crickets.service.*;
import jakarta.inject.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

@Path("/internal")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InternalResource {

    @Inject
    private ProfileService profileService;

    @POST
    @Path("/subscribe")
    public Response subscribe(SubscribeRequest request) {
        SubscribeResponse data = new SubscribeResponse();

        try {
            User subscriber = request.getSubscriber();
            String creatorName = request.getCreatorName();

            String message = profileService.subscribeLocal(subscriber, creatorName);
            data.setMessage(message);
        } catch (Exception e) {
            data.setError(e.getMessage());
        }

        return Response.ok(data).build();
    }

    // SubscribeRequest DTO
    public static class SubscribeRequest {
        private User subscriber;
        private String creatorName;

        // Getter und Setter
        public User getSubscriber() {
            return subscriber;
        }
        public void setSubscriber(User subscriber) {
            this.subscriber = subscriber;
        }
        public String getCreatorName() {
            return creatorName;
        }
        public void setCreatorName(String creatorName) {
            this.creatorName = creatorName;
        }
    }

    // SubscribeResponse DTO
    public static class SubscribeResponse {
        private String message;
        private String error;

        // Getter und Setter
        public String getMessage() {
            return message;
        }
        public void setMessage(String message) {
            this.message = message;
        }
        public String getError() {
            return error;
        }
        public void setError(String error) {
            this.error = error;
        }
    }

}
