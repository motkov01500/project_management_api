package org.cbg.projectmanagement.project_management.exception;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@ApplicationException
public class UsernameAlreadyExistsException extends WebApplicationException {

    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}
