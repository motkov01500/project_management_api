package org.cbg.projectmanagement.project_management.exception;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.WebApplicationException;

@ApplicationException
public class MeetingOutdatedException extends WebApplicationException {

    public MeetingOutdatedException(String message) {
        super(message);
    }
}
