package com.example.crickets.client;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.eclipse.microprofile.rest.client.inject.*;

import static com.example.crickets.resource.InternalResource.*;

@RegisterRestClient
public interface InternalServiceClient {

    @POST
    @Path("/subscribe")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    SubscribeResponse subscribe(SubscribeRequest request);

}
