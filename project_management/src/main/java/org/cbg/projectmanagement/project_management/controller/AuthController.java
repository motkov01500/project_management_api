package org.cbg.projectmanagement.project_management.controller;

import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
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
import org.cbg.projectmanagement.project_management.entity.User;
import org.cbg.projectmanagement.project_management.mapper.AuthMapper;
import org.cbg.projectmanagement.project_management.security.AuthenticationStoreImpl;
import org.cbg.projectmanagement.project_management.service.UserService;

@Path("/v1/auth")
public class AuthController {

    @Context
    private SecurityContext context;

    @Inject
    private AuthMapper authMapper;

    @Inject
    private UserService userService;

    @Inject
    private AuthenticationStoreImpl authenticationStore;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("login")
    public Response login(AuthLoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        try {
            CredentialValidationResult validationResult =
                    authenticationStore.validate(new UsernamePasswordCredential(username, password));
            if (validationResult.getStatus() == CredentialValidationResult.Status.VALID) {
                return Response
                        .status(Response.Status.OK)
                        .entity(authMapper
                                .mapUserToAuthResponseDTO(userService
                                        .getUserByUsername(context.getUserPrincipal().getName())))
                        .build();
            } else {
                return Response
                        .status(Response.Status.UNAUTHORIZED)
                        .entity("Fail")
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response
                .status(Response.Status.UNAUTHORIZED)
                .entity(Json.createObjectBuilder()
                        .add("message", "Invalid Username or Password")
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
