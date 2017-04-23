package com.mgl.dotsandboxes.server.model.game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.ImmutableList;
import com.mgl.dotsandboxes.server.model.player.PlayerKind;
import lombok.Data;
import lombok.experimental.Wither;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A snapshot ("status") of a game. <b>Not</b> mean necessarily terminated: this is just
 * the game status information at a point in time.
 */
@Data
public class GameSnapshot {

    @NotNull
    private final GameState state;

    @Min(0) @Wither
    private final long score;

    @NotNull @Valid
    private final LinkedList<TurnResult> turnResults;

    GameSnapshot(GameState state, long score, List<TurnResult> turnResults) {
        checkNotNull(state, "state");
        checkNotNull(turnResults, "turnResults");
        checkArgument(score >= 0, "Score cannot be negative");

        if (state.hasStarted() && turnResults.isEmpty()) {
            throw new IllegalArgumentException("A started game cannot have empty turns.");
        }

        this.state = state;
        this.score = score;
        this.turnResults = new LinkedList<>(turnResults);
    }

    @NotNull
    public Optional<PlayerKind> getWinner() {
        return isTerminated()
                ? Optional.of(getTurnResults().getLast().getPlayerKind())
                : Optional.empty();
    }

    public static GameSnapshot created() {
        return new GameSnapshot(GameState.CREATED, 0, ImmutableList.of());
    }

    public GameSnapshot resultingFromTurn(GameState state, TurnResult turnResult) {
        return new GameSnapshot(
                state, score,
                ImmutableList.<TurnResult>builder()
                        .addAll(getTurnResults())
                        .add(turnResult)
                        .build());
    }

    @JsonIgnore
    public boolean isTerminated() {
        return getState().hasTerminated();
    }

    @JsonIgnore
    public boolean isStarted() {
        return getState().hasStarted();
    }

    public boolean wasWonByHuman() {
        return getWinner().map(PlayerKind.HUMAN::equals).orElse(false);
    }

    @JsonIgnore
    public Board getCurrentBoard() {
        checkArgument(isStarted(), "Not yet started");
        return getTurnResults().getLast().getResultingBoard();
    }

    @JsonIgnore
    public Instant getLastTurnTs() {
        checkArgument(isStarted(), "Not yet started");
        return getTurnResults().getLast().getTs();
    }

    public PlayerKind whoTurnedLast() {
        checkArgument(isStarted(), "Not yet started");
        return getTurnResults().getLast().getPlayerKind();
    }

    @JsonIgnore
    public long getLastMoveId() {
        checkArgument(isStarted(), "Not yet started");
        return getTurnResults().getLast().getMoveId();
    }

    public int calculateNonConsecutiveTurnsCount(PlayerKind playerKind) {
        return (int) getTurnResults().stream()
                .filter(turnResult -> turnResult.getPlayerKind() == playerKind)
                .map(TurnResult::getMoveId)
                .distinct()
                .count();
    }

    // TODO: try to better solve this using a custom collector for Duration
    public Duration calculateThinkingTime(PlayerKind playerKind) {
        long millis = getTurnResults().stream()
                .filter(turnResult -> turnResult.getPlayerKind() == playerKind)
                .map(TurnResult::getThinkingTime)
                .collect(Collectors.summingLong(Duration::toMillis));
        return Duration.ofMillis(millis);
    }

}
