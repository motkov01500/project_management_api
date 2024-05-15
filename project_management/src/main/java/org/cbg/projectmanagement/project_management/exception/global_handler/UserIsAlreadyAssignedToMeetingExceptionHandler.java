package org.cbg.projectmanagement.project_management.exception.global_handler;

import jakarta.json.Json;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.cbg.projectmanagement.project_management.exception.UserIsAlreadyAssignedToMeetingException;

@Provider
public class UserIsAlreadyAssignedToMeetingExceptionHandler implements ExceptionMapper<UserIsAlreadyAssignedToMeetingException> {

    @Override
    public Response toResponse(UserIsAlreadyAssignedToMeetingException exception) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(Json.createObjectBuilder()
                        .add("meeting", exception.getMessage())
                        .build())
                .build();
    }
}
