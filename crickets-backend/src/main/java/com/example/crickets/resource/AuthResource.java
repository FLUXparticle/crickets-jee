package com.example.crickets.resource;

import com.example.crickets.dto.*;
import jakarta.servlet.http.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import static java.util.Collections.*;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthResource {

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(Credentials credentials, @Context HttpServletRequest request) {
        String username = credentials.getUsername();
        String password = credentials.getPassword();

        try {
            // Wenn ein Benutzer bereits angemeldet ist, abmelden
            try {
                request.logout();
            } catch (Exception e) {
                // Ignoriere Fehler beim Logout, wenn kein Benutzer eingeloggt ist
            }

            // Neuer Benutzer einloggen
            request.login(username, password);

            // Erfolgsantwort zurückgeben
            return Response.ok(singletonMap("message", "Login successful")).build();
        } catch (Exception e) {
            // Fehlerbehandlung bei ungültigen Anmeldedaten
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(singletonMap("error", e.getMessage()))
                    .build();
        }
    }

}
