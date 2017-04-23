package com.mgl.dotsandboxes.server.service.support;

public abstract class ServiceException extends RuntimeException {

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String format, Object ...args) {
        super(String.format(format, args));
    }

    public ServiceException(Exception cause, String format, Object ...args) {
        super(String.format(format, args), cause);
    }

}
