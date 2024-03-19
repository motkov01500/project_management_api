package org.cbg.projectmanagement.project_management.controller;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
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
import org.cbg.projectmanagement.project_management.dto.auth.AuthResponseDTO;
import org.cbg.projectmanagement.project_management.entity.User;
import org.cbg.projectmanagement.project_management.mapper.AuthMapper;
import org.cbg.projectmanagement.project_management.service.UserService;

import java.util.Optional;

@Path("/auth")
public class AuthController {

    @Context
    SecurityContext context;

    @Inject
    AuthMapper authMapper;

    @Inject
    UserService userService;

    @POST
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response login() {
        if(context.getUserPrincipal().getName() != null) {
            Optional<User> currentLoggedUser = userService
                    .getUserByUsername(context.getUserPrincipal().getName());
            AuthResponseDTO user = authMapper.mapUserToAuthResponseDTO(currentLoggedUser.get());
            return Response.ok(user).build();
        }
        return Response
                .status(Response.Status.UNAUTHORIZED)
                .build();
    }
}
