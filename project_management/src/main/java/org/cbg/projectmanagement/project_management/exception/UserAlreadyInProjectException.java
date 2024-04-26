package org.cbg.projectmanagement.project_management.exception;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@ApplicationException
public class UserAlreadyInProjectException extends WebApplicationException {

    public UserAlreadyInProjectException(String message) {
        super(message);
    }
}
