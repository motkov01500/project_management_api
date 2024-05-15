package org.cbg.projectmanagement.project_management.controller;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.json.Json;
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
    @Path("/administrator/get-all/{page}/{offset}")
    @RolesAllowed("administrator")
    public Response getAll(@PathParam("page") int page, @PathParam("offset") int offset) {
        return Response
                .status(Response.Status.OK)
                .entity(userService.findAll(page, offset)
                        .stream()
                        .map(userMapper::userToUserResponseDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/administrator/get-all-size")
    @RolesAllowed("administrator")
    public Response getAllSize() {
        return Response
                .status(Response.Status.OK)
                .entity(userService.findAllSize())
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/administrator/get-users-can-add-to-task/{taskId}")
    @RolesAllowed("administrator")
    public Response getUsersCanAddToTask(@PathParam("taskId") Long taskId) {
        return Response
                .status(Response.Status.OK)
                .entity(userService.findUsersNotAssignedToTask(taskId)
                        .stream()
                        .map(userMapper::userToUserResponseDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/administrator/is-user-can-assign-to-project/{projectKey}")
    @RolesAllowed("administrator")
    public Response isUserCanAssignToProject(@PathParam("projectKey") String projectKey) {
        return Response
                .status(Response.Status.OK)
                .entity(Json.createObjectBuilder()
                        .add("isUsersAvailable",userService.isUsersAvailableForAssignToProject(projectKey))
                        .build())
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/administrator/is-user-can-assign-to-task/{taskId}")
    @RolesAllowed("administrator")
    public Response isUserCanAssignToProject(@PathParam("taskId") Long taskId) {
        return Response
                .status(Response.Status.OK)
                .entity(Json.createObjectBuilder()
                        .add("isUsersAvailable",userService.isUsersAvailableForAssignToTak(taskId))
                        .build())
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/administrator/is-user-can-assign-to-meeting/{meetingId}")
    @RolesAllowed("administrator")
    public Response isUserCanAssignToMeeting(@PathParam("meetingId") Long meetingId) {
        return Response
                .status(Response.Status.OK)
                .entity(Json.createObjectBuilder()
                        .add("isUsersAvailable",userService.isUsersAvailableForAssignToMeeting(meetingId))
                        .build())
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/administrator/find-not-assigned-to-meeting-users/{meetingId}")
    @RolesAllowed("administrator")
    public Response getNotAssignedToMeeting(@PathParam("meetingId") Long meetingId) {
        return Response
                .status(Response.Status.OK)
                .entity(userService.findUsersNotAssignedToMeeting(meetingId)
                        .stream().map(userMapper::userToUserResponseDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/administrator/find-not-assigned-to-project-users/{projectKey}")
    @RolesAllowed("administrator")
    public Response getNotAssignedToProject(@PathParam("projectKey") String projectKey) {
        return Response
                .status(Response.Status.OK)
                .entity(userService.findUsersNotAssignedToProject(projectKey)
                        .stream().map(userMapper::userToUserResponseDTO)
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
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get-all-related-to-project/{key}/{page}/{offset}")
    @RolesAllowed({"user", "administrator"})
    public Response getUsersRelatedToProject(@PathParam("key") String key, @PathParam("page") int page,@PathParam("offset") int offset) {
        return Response
                .status(Response.Status.OK)
                .entity(userService.getUsersRelatedToProject(key, page, offset)
                        .stream()
                        .map(userMapper::userToUserResponseDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get-all-related-to-project-size/{key}")
    @RolesAllowed({"user", "administrator"})
    public Response getUsersRelatedToProject(@PathParam("key") String key) {
        return Response
                .status(Response.Status.OK)
                .entity(userService.getUsersRelatedToProjectSize(key))
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("current-user")
    @RolesAllowed({"user", "administrator"})
    public Response getCurrentUser() {
        return Response
                .status(Response.Status.OK)
                .entity(userMapper
                        .userToUserResponseDTO(userService
                                .getCurrentUser()))
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get-all-related-to-meeting/{meetingId}/{page}/{offset}")
    @RolesAllowed({"user", "administrator"})
    public Response getUsersRelatedToMeeting(@PathParam("meetingId") Long id, @PathParam("page") int page, @PathParam("offset") int offset) {
        return Response
                .status(Response.Status.OK)
                .entity(userService.getUsersRelatedToMeeting(id, page, offset)
                        .stream()
                        .map(userMapper::userToUserResponseDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get-all-related-to-meeting-size/{meetingId}")
    @RolesAllowed({"user", "administrator"})
    public Response getUsersRelatedToMeeting(@PathParam("meetingId") Long id) {
        return Response
                .status(Response.Status.OK)
                .entity(userService.getUsersRelatedToMeetingSize(id))
                .build();
    }

    @GET
    @Path("/get-all-related-to-task/{taskId}/{page}/{offset}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"administrator", "user"})
    public Response getAllRelatedToTask(@PathParam("taskId") Long taskId, @PathParam("page") int page,@PathParam("offset") int offset) {
        return Response
                .status(Response.Status.OK)
                .entity(userService
                        .findUsersRelatedToTask(taskId, page, offset)
                        .stream()
                        .map(userMapper::userToUserResponseDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @GET
    @Path("/get-all-related-to-task-size/{taskId}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"administrator", "user"})
    public Response getAllRelatedToTask(@PathParam("taskId") Long taskId) {
        return Response
                .status(Response.Status.OK)
                .entity(userService
                        .findUsersRelatedToTaskSize(taskId))
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
                .entity(userMapper
                        .userToUserResponseDTO(userService
                                .uploadImageToCurrentUser(userUpdateImageDTO)))
                .build();
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/update-password/{id}")
    @RolesAllowed("user")
    public Response updatePassword(@PathParam("id") Long id, UserPasswordUpdateDTO userPasswordUpdateDTO) {
        return Response
                .status(Response.Status.OK)
                .entity(userMapper
                        .userToUserResponseDTO(userService
                                .updatePassword(id, userPasswordUpdateDTO)))
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