package com.mgl.dotsandboxes.server.resources.player.exception;

import com.mgl.dotsandboxes.server.resources.support.ErrorResponse;
import com.mgl.dotsandboxes.server.service.player.exception.PlayerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PlayerNotFoundExceptionMapper {

    @ExceptionHandler(PlayerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse processException(PlayerNotFoundException ex) {
        return ErrorResponse.forHttpStatusCode(HttpStatus.NOT_FOUND, ex.getMessage());
    }

}
