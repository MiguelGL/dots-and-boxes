package com.mgl.dotsandboxes.server.model.player;

import com.google.common.base.Preconditions;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class PlayerRegistration {

    @NotBlank
    private final String playerId;

    public static PlayerRegistration ofPlayerId(String playerId) {
        Preconditions.checkNotNull(playerId, "playerId");
        Preconditions.checkArgument(!playerId.trim().isEmpty(), "Player id cannot be blank");
        return new PlayerRegistration(playerId);
    }

}
