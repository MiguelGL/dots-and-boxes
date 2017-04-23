package com.mgl.dotsandboxes.server.resources.game.exception;

import com.mgl.dotsandboxes.server.resources.support.ErrorResponse;
import com.mgl.dotsandboxes.server.service.game.exception.ConnectionAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ConnectionAlreadyExistsExceptionMapper {

    @ExceptionHandler(ConnectionAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorResponse processException(ConnectionAlreadyExistsException ex) {
        return ErrorResponse.forHttpStatusCode(HttpStatus.CONFLICT, ex.getMessage());
    }

}
