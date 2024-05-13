package org.cbg.projectmanagement.project_management.exception.global_handler;

import io.jsonwebtoken.JwtException;
import jakarta.json.Json;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class JWTExpireExceptionHandler implements ExceptionMapper<JwtException> {

    @Override
    public Response toResponse(JwtException e) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(Json.createObjectBuilder()
                        .add("message","Your credentials have expired.")
                        .build())
                .build();
    }
}
