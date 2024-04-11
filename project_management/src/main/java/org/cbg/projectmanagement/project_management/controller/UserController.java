package org.cbg.projectmanagement.project_management.controller;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.cbg.projectmanagement.project_management.dto.auth.AuthResponseDTO;
import org.cbg.projectmanagement.project_management.dto.user.UserCreateDTO;
import org.cbg.projectmanagement.project_management.dto.user.UserPasswordUpdateDTO;
import org.cbg.projectmanagement.project_management.dto.user.UserUpdateDTO;
import org.cbg.projectmanagement.project_management.dto.user.UserUpdateImageDTO;
import org.cbg.projectmanagement.project_management.entity.User;
import org.cbg.projectmanagement.project_management.mapper.AuthMapper;
import org.cbg.projectmanagement.project_management.mapper.UserMapper;
import org.cbg.projectmanagement.project_management.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Path("/v1/user")
public class UserController {

    @Inject
    private UserService userService;

    @Inject
    private UserMapper userMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/administrator/get-all")
    @RolesAllowed("administrator")
    public Response getAll() {
        return Response
                .status(Response.Status.OK)
                .entity(userService.findAll()
                        .stream()
                        .map(userMapper::userToUserResponseDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/administrator/get-by-id/{id}")
    @RolesAllowed("administrator")
    public Response getById(@PathParam("id") long id) {
        return Response
                .status(Response.Status.OK)
                .entity(userMapper
                        .userToUserResponseDTO(userService
                                .findUserById(id)))
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/administrator/get-all-unassigned")
    @RolesAllowed("administrator")
    public Response getUnassignedUsers() {
        return Response
                .status(Response.Status.OK)
                .entity(userService.getUnassignedUsers()
                        .stream()
                        .map(userMapper::userToUserResponseDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @GET
    @Path("/get-all-related-to-project/{key}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user", "administrator"})
    public Response getUsersRelatedToProject(@PathParam("key") String key) {
        return Response
                .status(Response.Status.OK)
                .entity(userService.getUsersRelatedToProject(key)
                        .stream()
                        .map(userMapper::userToUserResponseDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @GET
    @Path("current-user")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public Response getCurrentUser() {
        return Response
                .status(Response.Status.OK)
                .entity(userMapper
                        .userToUserResponseDTO(userService
                                .getCurrentUser()))
                .build();
    }

    @GET
    @Path("/get-all-related-to-meeting/{meetingId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("user")
    public Response getUsersRelatedToMeeting(@PathParam("meetingId") Long id) {
        return Response
                .status(Response.Status.OK)
                .entity(userService.getUsersRelatedToMeeting(id)
                        .stream()
                        .map(userMapper::userToUserResponseDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/administrator/create")
    @RolesAllowed("administrator")
    public Response create(UserCreateDTO userCreateDTO) {
        return Response
                .status(Response.Status.ACCEPTED)
                .entity(userMapper
                        .userToUserResponseDTO(userService.createUser(userCreateDTO)))
                .build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/administrator/update/{id}")
    @RolesAllowed("administrator")
    public Response update(@PathParam("id") Long id, UserUpdateDTO userUpdateDTO) {
        return Response
                .status(Response.Status.OK)
                .entity(userMapper
                        .userToUserResponseDTO(userService.updateUser(id, userUpdateDTO)))
                .build();
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/upload-image")
    @RolesAllowed("user")
    public Response uploadImageToCurrentUser(UserUpdateImageDTO userUpdateImageDTO) {
        return Response
                .status(Response.Status.OK)
                .entity(userService.uploadImageToCurrentUser(userUpdateImageDTO))
                .build();
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/update-password/{id}")
    @RolesAllowed("user")
    public Response updatePassword(@PathParam("id")Long id, UserPasswordUpdateDTO userPasswordUpdateDTO) {
        return Response
                .status(Response.Status.OK)
                .entity(userMapper
                        .userToUserResponseDTO(userService
                                .updatePassword(id,userPasswordUpdateDTO)))
                .build();
    }

    @DELETE
    @Path("/administrator/delete/{id}")
    @RolesAllowed("administrator")
    public Response deleteById(@PathParam("id") Long id) {
        userService.deleteUserById(id);
        return Response
                .status(Response.Status.NO_CONTENT)
                .build();
    }
}