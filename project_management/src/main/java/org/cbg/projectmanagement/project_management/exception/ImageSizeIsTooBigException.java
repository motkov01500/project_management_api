package org.cbg.projectmanagement.project_management.exception;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.WebApplicationException;

@ApplicationException
public class ImageSizeIsTooBigException extends WebApplicationException {

    public ImageSizeIsTooBigException(String message) {
        super(message);
    }
}
