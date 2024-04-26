package org.cbg.projectmanagement.project_management.exception;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.mapstruct.ap.internal.util.Message;

@ApplicationException
public class NotFoundResourceException extends WebApplicationException {

    public NotFoundResourceException(String message) {
        super(message);
    }
}
