package org.cbg.projectmanagement.project_management.exception;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.WebApplicationException;

@ApplicationException
public class DeleteAdminUserException extends WebApplicationException {

    public DeleteAdminUserException(String message) {
        super(message);
    }
}
