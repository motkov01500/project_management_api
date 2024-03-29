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
    @Path("/administrator/get-all")
    @RolesAllowed("administrator")
    public Response getAll() {
        return Response
                .status(Response.Status.OK)
                .entity(userService.findAllUsers()
                        .stream()
                        .map(authMapper::mapUserToAuthResponseDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/administrator/get-all-unassigned/{key}")
    @RolesAllowed("administrator")
    public Response getUnassignedUsers(@PathParam("key")String key) {
        return Response
                .status(Response.Status.OK)
                .entity(userService.getUnassignedUsers(key)
                        .stream()
                        .map(authMapper::mapUserToAuthResponseDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/administrator/create")
    @RolesAllowed("administrator")
    public Response create(UserCreateDTO userCreateDTO) {
        return Response
                .status(Response.Status.ACCEPTED)
                .entity(authMapper
                        .mapUserToAuthResponseDTO(userService.createUser(userCreateDTO)))
                .build();
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/update/{id}")
    @PermitAll
    public Response update(@PathParam("id") Long id, UserUpdateDTO userUpdateDTO) {
        return Response
                .status(Response.Status.OK)
                .entity(authMapper
                        .mapUserToAuthResponseDTO(userService.updateUser(id, userUpdateDTO)))
                .build();
    }

    @DELETE
    @Path("/delete/{id}")
    @RolesAllowed("administrator")
    public Response deleteById(@PathParam("id") Long id) {
        userService.deleteUserById(id);
        return Response
                .status(Response.Status.NO_CONTENT)
                .build();
    }
}