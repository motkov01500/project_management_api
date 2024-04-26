package org.cbg.projectmanagement.project_management.controller;

import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.cbg.projectmanagement.project_management.dto.auth.AuthLoginDTO;
import org.cbg.projectmanagement.project_management.dto.auth.RegisterDTO;
import org.cbg.projectmanagement.project_management.mapper.AuthMapper;
import org.cbg.projectmanagement.project_management.service.UserService;

@Path("/v1/auth")
public class AuthController {

    @Context
    private SecurityContext context;

    @Inject
    private AuthMapper authMapper;

    @Inject
    private UserService userService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("login")
    public Response login(AuthLoginDTO authLoginDTO) {
        if(context.getUserPrincipal().getName() != null) {
            return Response
                    .status(Response.Status.OK)
                    .entity(authMapper
                            .mapUserToAuthResponseDTO(userService
                                    .getUserByUsername(context.getUserPrincipal().getName())))
                    .build();
        }
        return Response
                .status(Response.Status.UNAUTHORIZED)
                .entity(Json.createObjectBuilder()
                        .add("message","Invalid Username or Password")
                        .build())
                .build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("register")
    public Response register(RegisterDTO registerDTO) {
        return Response
                .status(Response.Status.OK)
                .entity(authMapper
                        .mapUserToAuthResponseDTO(userService
                                .register(registerDTO)))
                .build();
    }
}
