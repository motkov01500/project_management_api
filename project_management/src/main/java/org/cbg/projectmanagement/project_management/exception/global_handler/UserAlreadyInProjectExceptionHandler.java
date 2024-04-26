package org.cbg.projectmanagement.project_management.exception.global_handler;

import jakarta.json.Json;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.cbg.projectmanagement.project_management.exception.UserAlreadyInProjectException;

public class UserAlreadyInProjectExceptionHandler implements ExceptionMapper<UserAlreadyInProjectException> {

    @Override
    public Response toResponse(UserAlreadyInProjectException e) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(Json.createObjectBuilder()
                        .add("message",e.getMessage())
                        .build())
                .build();
    }
}
