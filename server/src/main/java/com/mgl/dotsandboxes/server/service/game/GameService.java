package com.mgl.dotsandboxes.server.service.game;

import com.google.common.collect.Sets;
import com.mgl.dotsandboxes.server.model.game.Box;
import com.mgl.dotsandboxes.server.model.game.GameSnapshot;
import com.mgl.dotsandboxes.server.model.game.TurnResult;
import com.mgl.dotsandboxes.server.model.player.PlayerKind;
import com.mgl.dotsandboxes.server.repository.game.GameRepository;
import com.mgl.dotsandboxes.server.service.player.PlayerService;
import com.mgl.dotsandboxes.server.model.game.Board;
import com.mgl.dotsandboxes.server.model.game.Connection;
import com.mgl.dotsandboxes.server.model.game.Dot;
import com.mgl.dotsandboxes.server.model.game.Game;
import com.mgl.dotsandboxes.server.model.game.GameState;
import com.mgl.dotsandboxes.server.service.game.exception.ConnectionAlreadyExistsException;
import com.mgl.dotsandboxes.server.service.game.exception.ForbiddenGameAccessException;
import com.mgl.dotsandboxes.server.service.game.exception.GameNotFoundException;
import com.mgl.dotsandboxes.server.service.game.exception.GameNotTerminatedException;
import com.mgl.dotsandboxes.server.service.game.exception.IllegalConnectionException;
import com.mgl.dotsandboxes.server.service.game.exception.UnexpectedGameConditionException;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class GameService {

    private final @NonNull
    GameRepository gameRepository;

    private final @NonNull
    PlayerService playerService;
    private final @NonNull ScoreService scoreService;

    private Random random;

    @PostConstruct
    public void init() {
        random = new Random();
    }

    private Game findPlayerOwnedGame(String playerId, String gameId) {
        Optional<Game> maybeGame = gameRepository.maybeFindByGameId(gameId);
        Game game = maybeGame.orElseThrow(() ->
                new GameNotFoundException(gameId));
        if (!game.getPlayerId().equals(playerId)) {
            throw new ForbiddenGameAccessException(gameId, playerId);
        }
        return game;
    }

    private void ensurePlayerOwnsAndWonExistingTerminatedGame(String playerId, String gameId) {
        Game game = findPlayerOwnedGame(playerId, gameId);
        if (!game.isTerminated()) {
            throw new GameNotTerminatedException(gameId);
        } else if (!game.wasWonByHuman()) {
            throw new UnexpectedGameConditionException(
                    "Game '%s' was not won by human player", gameId);
        }
    }

    public Game createNewGame(
            String playerId, int dimension, PlayerKind whoStarts,
            Optional<String> maybeAsFollowUpTo) {

        playerService.ensurePlayerIdExists(playerId);

        maybeAsFollowUpTo.ifPresent(asFollowUpToGameId ->
                ensurePlayerOwnsAndWonExistingTerminatedGame(playerId, asFollowUpToGameId));

        String gameId = UUID.randomUUID().toString();
        Game game = new Game(gameId, playerId, whoStarts, dimension, Instant.now());

        switch (whoStarts) {
            case COMPUTER:
                playRandomComputerTurns(game);
                break;
            case HUMAN:
                break;
            default:
                throw new UnsupportedOperationException(String.valueOf(whoStarts));
        }

        gameRepository.create(game);

        return game;
    }

    public List<Game> getPlayerGames(String playerId) {
        playerService.ensurePlayerIdExists(playerId);
        return gameRepository.getPlayerGames(playerId);
    }

    public Game getPlayerGame(String playerId, String gameId) {
        playerService.ensurePlayerIdExists(playerId);
        return findPlayerOwnedGame(playerId, gameId);
    }

    // Just an dirty utility class for horizontal/vertical connection modeling.
    @ToString
    @EqualsAndHashCode(callSuper = true)
    static class HzOrVzLine extends Dot {

        final boolean hz;

        public HzOrVzLine(int x, int y, boolean hz) {
            super(x, y);
            this.hz = hz;
        }
    }

    static Connection asConnection(HzOrVzLine line) {
        Dot d0;
        Dot d1;
        if (line.hz) {
            d0 = Dot.of(line.getX(), line.getY());
            d1 = Dot.of(line.getX() + 1, line.getY());
        } else {
            d0 = Dot.of(line.getX(), line.getY());
            d1 = Dot.of(line.getX(), line.getY() + 1);
        }
        return Connection.of(d0, d1);
    }

    // This method is just a quick hack to try to randomly play COMPUTER. It is not
    // a clever method of any kind. I am conciously going the easy route here for simplicity.
    private void playRandomComputerTurns(Game game) {
        log.info("{} plays...", PlayerKind.COMPUTER);

        Board currentBoard = game.getCurrentBoard();
        int dim = currentBoard.getDimension();

        int rndOffsetI = random.nextInt(dim + 1);
        int rndOffsetJ = random.nextInt(dim + 1);
        int hzVzOffset = random.nextInt(2);

        Connection connection = null;
        for (int k = 0; k < 2; k++) {
            boolean hz = (k + hzVzOffset) % 2 == 0;
            int topI = hz ? dim : dim + 1;
            int topJ = hz ? dim + 1 : dim;
            for (int i = 0; i < topI; i++) {
                for (int j = 0; j < topJ; j++) {
                    int x = (i + rndOffsetI) % (topI);
                    int y = (j + rndOffsetJ) % (topJ);
                    Connection candidate = asConnection(new HzOrVzLine(x, y, hz));
                    boolean available = currentBoard.isConnectionAvailable(candidate);
                    log.debug("connection {} available: {}", candidate.asDotsString(), available);
                    if (available) {
                        connection = candidate;
                    }
                }
            }
        }

        if (connection == null) {
            throw new IllegalStateException("No more available lines.");
        }

        boolean canFurtherGo = playTurn(game, PlayerKind.COMPUTER, connection);
        if (canFurtherGo) {
            playRandomComputerTurns(game);
        }
    }

    private boolean playTurn(Game game, PlayerKind playerKind, Connection connection) {
        log.info("~~~ {} plays: {}", playerKind, connection.asDotsString());

        Instant ts = Instant.now();

        Board currentBoard = game.getCurrentBoard();

        if (!currentBoard.isFurtherPlayable()) {
            throw new UnexpectedGameConditionException("Game '%s' is not further playable",
                    game.getGameId());
        } else if (currentBoard.hasConnection(connection)) {
            throw new ConnectionAlreadyExistsException(game.getGameId(), connection);
        }

        Board resultingBoard;
        try {
            resultingBoard = currentBoard.withConnection(connection);
        } catch (IllegalArgumentException e) {
            throw new IllegalConnectionException(game.getGameId(), connection, e);
        }

        if (log.isInfoEnabled()) { // Avoid computing ascii if level not enabled
            log.info("Original Board\n{}", currentBoard.toAsciiArt());
            log.info("Resulting Board\n{}", resultingBoard.toAsciiArt());
        }

        Set<Box> currentBoxes = currentBoard.calculateBoxes();
        Set<Box> resultingBoxes = resultingBoard.calculateBoxes();
        Set<Box> madeBoxes = Sets.difference(resultingBoxes, currentBoxes);

        Duration timeTaken = Duration.between(game.getLastTurnTs(), ts);

        long currentMoveId = game.getLastMoveId();

        long moveId = game.wasLastPlayedBy(playerKind) ? currentMoveId : currentMoveId + 1;

        TurnResult turn = new TurnResult(
                playerKind, moveId, timeTaken, ts, resultingBoard, madeBoxes);

        GameState gameState = resultingBoard.isFurtherPlayable()
                ? GameState.ONGOING
                : GameState.TERMINATED;

        GameSnapshot resultingSnapshot =
                game.getCurrentSnapshot().resultingFromTurn(gameState, turn);

        long score = scoreService.calculateScore(
                playerKind,
                resultingSnapshot.calculateNonConsecutiveTurnsCount(playerKind),
                resultingSnapshot.calculateThinkingTime(playerKind));

        game.setCurrentSnapshot(resultingSnapshot.withScore(score));

        log.info("~~~ {} played! terminated: {}; made boxes: {}",
                playerKind, game.isTerminated(), turn.madeAnyBoxes());

        return !game.isTerminated() && turn.madeAnyBoxes();
    }

    public GameSnapshot playHumanTurn(String playerId, String gameId, Connection connection) {
        log.info("{} plays...", PlayerKind.HUMAN);
        Game game = getPlayerGame(playerId, gameId);
        boolean canFurtherGo = playTurn(game, PlayerKind.HUMAN, connection);
        if (!game.isTerminated() && !canFurtherGo) {
            playRandomComputerTurns(game);
        }
        return game.getCurrentSnapshot();
    }

}
