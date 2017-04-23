package com.mgl.dotsandboxes.server.model.game;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;

@Data
public class ScoringSummary {

    @NotBlank
    private final String gameId;

    @Min(0)
    private final long score;

    public static ScoringSummary fromGame(Game game) {
        return new ScoringSummary(game.getGameId(), game.getScore());
    }

}
