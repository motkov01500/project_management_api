package org.cbg.projectmanagement.project_management.exception;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.WebApplicationException;

@ApplicationException
public class UserIsAlreadyAssignedToMeetingException  extends WebApplicationException {

    public UserIsAlreadyAssignedToMeetingException(String message) {
        super(message);
    }
}
