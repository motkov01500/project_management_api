package org.cbg.projectmanagement.project_management.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.cbg.projectmanagement.project_management.dto.TokenResponseDTO;
import org.cbg.projectmanagement.project_management.request.AuthRequest;
import org.cbg.projectmanagement.project_management.service.AuthService;

@Path("/auth")
public class AuthController {

    @Inject
    AuthService authService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get-current")
    public Response getCurrentLoginUser() {
        return Response
                .status(Response.Status.OK)
                .entity(authService.getCurrentUserRole())
                .build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/login")
    public Response login(AuthRequest authRequest) {
        String token = authService.validateCredentials(authRequest);
        if(token.equals("NOT_VALID_CREDENTIALS")) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("NOT VALID CREDENTIALS")
                    .build();
        }

        return Response
                .status(Response.Status.OK)
                .entity(new TokenResponseDTO(token))
                .build();
    }
}
