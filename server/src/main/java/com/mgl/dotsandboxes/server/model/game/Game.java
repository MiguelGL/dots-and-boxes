package com.mgl.dotsandboxes.server.model.game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Preconditions;
import com.mgl.dotsandboxes.server.model.player.PlayerKind;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.Instant;

import static com.google.common.base.Preconditions.checkNotNull;

@Data
@EqualsAndHashCode(of = "gameId")
public class Game {

    @NotBlank
    private final String gameId;

    @NotBlank
    private final String playerId;

    @NonNull
    private final PlayerKind starterPlayer;

    @NotNull
    private final Instant creationTs;

    @Min(Board.MIN_DIM)
    private final int dimension;

    @NotNull @Valid
    private GameSnapshot currentSnapshot;

    public Game(String gameId, String playerId, PlayerKind starterPlayer,
                int dimension, Instant creationTs) {
        this.gameId = checkNotNull(gameId, "gameId");
        this.playerId = checkNotNull(playerId, "playerId");
        this.starterPlayer = checkNotNull(starterPlayer, "starterPlayer");
        Preconditions.checkArgument(dimension >= Board.MIN_DIM,
                "Dimension (%s) needs to be equal or greater than %s",
                dimension, Board.MIN_DIM);
        this.creationTs = checkNotNull(creationTs, "creationTs");
        this.dimension = dimension;
        this.currentSnapshot = GameSnapshot.created();
    }

    @JsonIgnore
    public boolean isTerminated() {
        return getCurrentSnapshot().isTerminated();
    }

    @JsonIgnore
    public Board getCurrentBoard() {
        return getCurrentSnapshot().isStarted()
                ? getCurrentSnapshot().getCurrentBoard()
                : Board.ofDimension(dimension);
    }

    @JsonIgnore
    public Instant getLastTurnTs() {
        return getCurrentSnapshot().isStarted()
                ? getCurrentSnapshot().getLastTurnTs()
                : getCreationTs();
    }

    public boolean wasWonByHuman() {
        return getCurrentSnapshot().wasWonByHuman();
    }

    private PlayerKind whoTurnedLast() {
        return getCurrentSnapshot().isStarted()
                ? getCurrentSnapshot().whoTurnedLast()
                : getStarterPlayer();
    }

    public boolean wasLastPlayedBy(PlayerKind playerKind) {
        return playerKind == whoTurnedLast();
    }

    @JsonIgnore
    public long getScore() {
        return getCurrentSnapshot().getScore();
    }

    @JsonIgnore
    public long getLastMoveId() {
        return getCurrentSnapshot().isStarted()
                ? getCurrentSnapshot().getLastMoveId()
                : 1L;
    }

}
