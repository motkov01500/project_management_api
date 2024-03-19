package org.cbg.projectmanagement.project_management.controller;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.cbg.projectmanagement.project_management.dto.auth.AuthResponseDTO;
import org.cbg.projectmanagement.project_management.dto.user.UserCreateDTO;
import org.cbg.projectmanagement.project_management.dto.user.UserUpdateDTO;
import org.cbg.projectmanagement.project_management.entity.User;
import org.cbg.projectmanagement.project_management.mapper.AuthMapper;
import org.cbg.projectmanagement.project_management.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Path("/user")
public class UserController {

    @Inject
    private UserService userService;

    @Inject
    private AuthMapper authMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getAll")
    @RolesAllowed("administrator")
    public Response getAllUsers() {
        List<AuthResponseDTO> userList = userService.findAllUsers()
                .stream()
                .map(authMapper::mapUserToAuthResponseDTO)
                .collect(Collectors.toList());
        return Response
                .status(Response.Status.OK)
                .entity(userList)
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/create")
    @RolesAllowed("administrator")
    public Response createUser(UserCreateDTO userCreateDTO) {
        User user = userService.createUser(userCreateDTO);
        AuthResponseDTO mappedUser = authMapper
                .mapUserToAuthResponseDTO(user);
        return Response
                .status(Response.Status.ACCEPTED)
                .entity(mappedUser)
                .build();
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/update/{id}")
    @PermitAll
    public Response updatePassword(@PathParam("id") Long id, UserUpdateDTO userUpdateDTO) {
        User user = userService
                .updateUser(userUpdateDTO, id);
        AuthResponseDTO mappedUser = authMapper
                .mapUserToAuthResponseDTO(user);
        return Response
                .status(Response.Status.OK)
                .entity(mappedUser)
                .build();
    }

    @DELETE
    @Path("/delete/{id}")
    @RolesAllowed("administrator")
    public Response deleteUserById(@PathParam("id") Long id) {
        userService.deleteUserById(id);
        return Response
                .status(Response.Status.NO_CONTENT)
                .build();
    }
}