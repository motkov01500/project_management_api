package org.cbg.projectmanagement.project_management.exception.global_handler;

import jakarta.json.Json;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.cbg.projectmanagement.project_management.exception.MeetingOutdatedException;

@Provider
public class MeetingOutdatedExceptionHandler implements ExceptionMapper<MeetingOutdatedException> {

    @Override
    public Response toResponse(MeetingOutdatedException exception) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(Json.createObjectBuilder()
                        .add("message",exception.getMessage())
                        .build())
                .build();
    }
}
