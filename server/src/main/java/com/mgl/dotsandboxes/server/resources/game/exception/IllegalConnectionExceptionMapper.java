package com.mgl.dotsandboxes.server.resources.game.exception;

import com.mgl.dotsandboxes.server.resources.support.ErrorResponse;
import com.mgl.dotsandboxes.server.service.game.exception.IllegalConnectionException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class IllegalConnectionExceptionMapper {

    @ExceptionHandler(IllegalConnectionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse processException(IllegalConnectionException ex) {
        return ErrorResponse.forHttpStatusCode(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

}
