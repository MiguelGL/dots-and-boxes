package com.mgl.dotsandboxes.server.service.game.exception;

import com.mgl.dotsandboxes.server.model.game.Connection;
import com.mgl.dotsandboxes.server.service.support.ServiceException;
import lombok.Getter;

public class ConnectionAlreadyExistsException extends ServiceException {

    @Getter private final String gameId;
    @Getter private final Connection connection;

    public ConnectionAlreadyExistsException(String gameId, Connection connection) {
        super("Game '%s' already has connection %s", gameId, connection.asDotsString());
        this.gameId = gameId;
        this.connection = connection;
    }

}
