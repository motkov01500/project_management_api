package org.cbg.projectmanagement.project_management.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public class ValidationException extends WebApplicationException {

    public ValidationException(String message, Response response) {
        super(message, response);
    }
}
