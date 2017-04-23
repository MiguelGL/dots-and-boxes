package com.mgl.dotsandboxes.server.model.game;

import com.mgl.dotsandboxes.server.model.player.PlayerKind;
import lombok.Data;
import lombok.NonNull;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.Instant;
import java.util.Set;

/**
 * The output after a player turned.
 */
@Data
public class TurnResult {

    @NotNull
    private final PlayerKind playerKind;

    @Min(0)
    private final long moveId;

    @NonNull
    private final Duration thinkingTime;

    @NotNull
    private final Instant ts;

    @NotNull @Valid
    private final Board resultingBoard;

    @NotNull
    private final Set<Box> boxes;

    public boolean madeAnyBoxes() {
        return getBoxes().size() > 0;
    }

}
