package com.example.crickets.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import static java.util.Collections.*;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Context
    private SecurityContext securityContext;

    @GET
    @Path("/username")
    public Response username() {
        try {
            String username = getUsername();
            return Response.ok(singletonMap("username", username)).build();
        } catch (Exception e) {
            return Response.ok(singletonMap("error", e.getMessage()))
                    .build();
        }
    }

    String getUsername() {
        return securityContext.getUserPrincipal().getName();
    }

}
