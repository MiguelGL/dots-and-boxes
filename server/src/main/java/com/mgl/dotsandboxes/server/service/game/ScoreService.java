package com.mgl.dotsandboxes.server.service.game;

import com.mgl.dotsandboxes.server.model.game.Game;
import com.mgl.dotsandboxes.server.model.game.ScoringSummary;
import com.mgl.dotsandboxes.server.model.player.PlayerKind;
import com.mgl.dotsandboxes.server.repository.game.GameRepository;
import com.mgl.dotsandboxes.server.service.player.PlayerService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ScoreService {

    private static final int TOP_TEN_MAX_RESULTS = 10; // Quite obviously :)

    private final @NonNull
    GameRepository gameRepository;

    private final @NonNull
    PlayerService playerService;

    public long calculateScore(PlayerKind playerKind, int turnsCount, Duration timeTaken) {
        // No fancy score calculator
        log.info("Calculating score for {}; duration: {}, turnsCount: {}",
                playerKind, timeTaken.toMillis(), turnsCount);
        return playerKind.computesScore() ? timeTaken.toMillis() / turnsCount : 0L;
    }

    public List<ScoringSummary> calculateTopTen(String playerId) {
        playerService.ensurePlayerIdExists(playerId);
        List<Game> games = gameRepository.findPlayerWonGamesSortedByScore(
                playerId, TOP_TEN_MAX_RESULTS);
        return games.stream().map(ScoringSummary::fromGame).collect(Collectors.toList());
    }

}
