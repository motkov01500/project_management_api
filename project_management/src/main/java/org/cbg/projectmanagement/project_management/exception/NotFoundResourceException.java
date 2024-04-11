package org.cbg.projectmanagement.project_management.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

public class NotFoundResourceException extends WebApplicationException {

    public NotFoundResourceException(Response response) {
        super(response);
    }
}
