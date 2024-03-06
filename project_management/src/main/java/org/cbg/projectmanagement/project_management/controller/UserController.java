package org.cbg.projectmanagement.project_management.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.cbg.projectmanagement.project_management.entity.User;
import org.cbg.projectmanagement.project_management.exception.NotFoundResourceException;
import org.cbg.projectmanagement.project_management.request.UpdatePasswordRequest;
import org.cbg.projectmanagement.project_management.request.UserRequest;
import org.cbg.projectmanagement.project_management.service.UserService;

import java.util.List;

@Path("/user")
public class UserController {

    @Inject
    private UserService userService;

    @GET
    @Produces("application/json")
    @Path("/getAll")
    public List<User> getAllUsers() {
        return userService.findAllUsers();
    }

    @POST
    @Consumes("application/json")
    @Path("/create")
    public Response createUser(UserRequest userRequest) {
        userService.createUser(userRequest);
        return Response
                .status(Response.Status.ACCEPTED)
                .build();
    }

    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/update-password")
    public Response updatePassword(UpdatePasswordRequest request) {
        User user = userService.updateUserPassword(request);
        return Response.status(Response.Status.OK).entity("Updated successfully").build();
    }

    @DELETE
    @Path("/delete/{id}")
    public Response deleteUserById(@PathParam("id")Long id) {
        userService.deleteUserById(id);
        return Response.status(Response.Status.NO_CONTENT).entity("Deleted successfully").build();
    }
}