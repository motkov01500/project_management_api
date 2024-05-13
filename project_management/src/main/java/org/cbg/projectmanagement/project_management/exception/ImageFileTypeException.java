package org.cbg.projectmanagement.project_management.exception;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.WebApplicationException;

@ApplicationException
public class ImageFileTypeException extends WebApplicationException {

    public ImageFileTypeException(String message) {
        super(message);
    }
}
