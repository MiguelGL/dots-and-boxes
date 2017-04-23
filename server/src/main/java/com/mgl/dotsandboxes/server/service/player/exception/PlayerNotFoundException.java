package com.mgl.dotsandboxes.server.service.player.exception;

import com.mgl.dotsandboxes.server.service.support.ServiceException;
import lombok.Getter;
import lombok.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

@Getter
public class PlayerNotFoundException extends ServiceException {

    private final @NonNull String playerId;

    public PlayerNotFoundException(String playerId) {
        super("Player with id '%s' does not exist", checkNotNull(playerId, "playerId"));
        this.playerId = playerId;
    }

}
