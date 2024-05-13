package org.cbg.projectmanagement.project_management.exception;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.WebApplicationException;

@ApplicationException
public class UserAlreadyAssignedToTaskException extends WebApplicationException {

    public UserAlreadyAssignedToTaskException(String message) {
        super(message);
    }
}
