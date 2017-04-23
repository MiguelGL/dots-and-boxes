package com.mgl.dotsandboxes.server.service.game.exception;

import com.mgl.dotsandboxes.server.model.game.Connection;
import com.mgl.dotsandboxes.server.service.support.ServiceException;
import lombok.Getter;

public class IllegalConnectionException extends ServiceException {

    @Getter private final String gameId;
    @Getter private final Connection connection;

    public IllegalConnectionException(String gameId, Connection connection, Exception cause) {
        super(cause, "Game '%s' does not allow connection %s", gameId, connection.asDotsString());
        this.gameId = gameId;
        this.connection = connection;
    }

}
