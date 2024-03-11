package org.cbg.projectmanagement.project_management.controller;

import jakarta.annotation.security.PermitAll;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("/auth")
public class AuthController {

    @Context
    SecurityContext context;

    @POST
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response login() {
        if(context.getUserPrincipal().getName() != null) {
            JsonObject result = Json.createObjectBuilder()
                    .add("user", context.getUserPrincipal().getName())
                    .build();
            return Response.ok(result).build();
        }
        return Response
                .status(Response.Status.UNAUTHORIZED)
                .build();
    }
}
