package org.cbg.projectmanagement.project_management.exception.global_handler;

import jakarta.json.Json;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.cbg.projectmanagement.project_management.exception.NotFoundResourceException;

@Provider
public class NotFoundResourceExceptionHandler implements ExceptionMapper<NotFoundResourceException> {

    @Override
    public Response toResponse(NotFoundResourceException e) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(Json.createObjectBuilder()
                        .add("message", e.getMessage())
                        .build())
                .build();
    }
}