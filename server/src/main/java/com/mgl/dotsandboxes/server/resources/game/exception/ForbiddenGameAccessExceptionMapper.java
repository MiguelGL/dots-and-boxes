package com.mgl.dotsandboxes.server.resources.game.exception;

import com.mgl.dotsandboxes.server.resources.support.ErrorResponse;
import com.mgl.dotsandboxes.server.service.game.exception.ForbiddenGameAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ForbiddenGameAccessExceptionMapper {

    @ExceptionHandler(ForbiddenGameAccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorResponse processException(ForbiddenGameAccessException ex) {
        return ErrorResponse.forHttpStatusCode(HttpStatus.FORBIDDEN, ex.getMessage());
    }

}
