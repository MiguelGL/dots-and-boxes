package com.mgl.dotsandboxes.server.service.game.exception;

import com.mgl.dotsandboxes.server.service.support.ServiceException;
import lombok.Getter;
import lombok.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

@Getter
public class GameNotFoundException extends ServiceException {

    private final @NonNull String gameId;

    public GameNotFoundException(String gameId) {
        super("Game with id '%s' does not exist", checkNotNull(gameId, "gameId"));
        this.gameId = gameId;
    }

}
