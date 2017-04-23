package com.mgl.dotsandboxes.server.service.game.exception;

import com.mgl.dotsandboxes.server.service.support.ServiceException;
import lombok.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

public class ForbiddenGameAccessException extends ServiceException {

    private final @NonNull String gameId;
    private final @NonNull String playerId;

    public ForbiddenGameAccessException(String gameId, String playerId) {
        super("Player '%s' is not the owner of game '%s'",
                checkNotNull(playerId, "playerId"), checkNotNull(gameId, "gameId"));
        this.gameId = gameId;
        this.playerId = playerId;
    }
}
