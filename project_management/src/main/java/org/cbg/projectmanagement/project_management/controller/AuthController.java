package org.cbg.projectmanagement.project_management.controller;

import com.sun.net.httpserver.HttpContext;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.cbg.projectmanagement.project_management.auth.AuthService;
import org.cbg.projectmanagement.project_management.auth.UserContext;
import org.cbg.projectmanagement.project_management.auth.UserDetails;
import org.cbg.projectmanagement.project_management.entity.User;
import org.cbg.projectmanagement.project_management.exception.NotFoundResourceException;
import org.cbg.projectmanagement.project_management.request.AuthRequest;
import org.cbg.projectmanagement.project_management.request.UserRequest;
import org.cbg.projectmanagement.project_management.service.UserService;

@Path("/auth")
public class AuthController {

    @Inject
    AuthService authService;

    @Inject
    UserService userService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(AuthRequest authRequest) throws NotFoundResourceException {
        User loginUser = authService.isValidCredentials(authRequest);
        UserDetails userDetails = new UserDetails(loginUser.getUsername(),
                loginUser.getRole().getName());

        return Response.status(Response.Status.OK).entity(userDetails).build();
    }
}
