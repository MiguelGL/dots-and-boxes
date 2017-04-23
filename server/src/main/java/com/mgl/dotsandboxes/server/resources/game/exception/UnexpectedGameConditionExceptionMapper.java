package com.mgl.dotsandboxes.server.resources.game.exception;

import com.mgl.dotsandboxes.server.resources.support.ErrorResponse;
import com.mgl.dotsandboxes.server.service.game.exception.GameNotTerminatedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UnexpectedGameConditionExceptionMapper {

    @ExceptionHandler(GameNotTerminatedException.class)
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    @ResponseBody
    public ErrorResponse processException(GameNotTerminatedException ex) {
        return ErrorResponse.forHttpStatusCode(HttpStatus.PRECONDITION_FAILED, ex.getMessage());
    }

}
