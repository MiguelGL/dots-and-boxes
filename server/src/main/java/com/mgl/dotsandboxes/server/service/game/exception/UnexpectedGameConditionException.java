package com.mgl.dotsandboxes.server.service.game.exception;

import com.mgl.dotsandboxes.server.service.support.ServiceException;

public class UnexpectedGameConditionException extends ServiceException {

    public UnexpectedGameConditionException(String format, Object... args) {
        super(format, args);
    }

}
