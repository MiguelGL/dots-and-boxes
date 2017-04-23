package com.mgl.dotsandboxes.server.service.game.exception;

import com.mgl.dotsandboxes.server.service.support.ServiceException;
import lombok.Getter;
import lombok.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

@Getter
public class GameNotTerminatedException extends ServiceException {

    private final @NonNull String gameId;

    public GameNotTerminatedException(String gameId) {
        super("Game '%s' is not terminated", checkNotNull(gameId, "gameId"));
        this.gameId = gameId;
    }
}
