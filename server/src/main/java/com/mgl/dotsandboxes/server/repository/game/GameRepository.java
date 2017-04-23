package com.mgl.dotsandboxes.server.repository.game;

import com.google.common.base.Preconditions;
import com.mgl.dotsandboxes.server.model.game.Game;
import lombok.Synchronized;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class GameRepository {

    private final Set<Game> games = Collections.synchronizedSet(new HashSet<>());

    public Optional<Game> maybeFindByGameId(String gameId) {
        return games.stream().filter(game ->  game.getGameId().equals(gameId)).findAny();
    }

    @Synchronized("games")
    public void create(Game game) {
        Preconditions.checkArgument(!games.contains(game), "Game '%s' already exists", game);
        games.add(game);
    }

    @Synchronized("games")
    public List<Game> getPlayerGames(String playerId) {
        return games.stream()
                .filter(game -> game.getPlayerId().equals(playerId))
                .sorted(Comparator.comparing(Game::getCreationTs))
                .collect(Collectors.toList());
    }

    public List<Game> findPlayerWonGamesSortedByScore(String playerId, int limit) {
        return games.stream()
                .filter(game -> game.getPlayerId().equals(playerId))
                .filter(game -> game.wasWonByHuman())
                .sorted(Comparator.comparing(Game::getScore).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

}
