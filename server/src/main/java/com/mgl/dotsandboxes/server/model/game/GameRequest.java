package com.mgl.dotsandboxes.server.model.game;

import com.mgl.dotsandboxes.server.model.player.PlayerKind;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class GameRequest {

    @Min(Board.MIN_DIM)
    private final int dimension;

    @NotNull
    private final PlayerKind whoStarts;

}
