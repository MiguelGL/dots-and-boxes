package com.mgl.dotsandboxes.server.resources.game;

import com.mgl.dotsandboxes.server.resources.player.PlayersResource;
import com.mgl.dotsandboxes.server.model.game.Game;
import com.mgl.dotsandboxes.server.service.game.GameService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/games/{gameId}")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GameResource {

    private final @NonNull GameService gameService;

    /**
     * Gets a game of a player (terminated or not).
     *
     * Relevant HTTP status codes:
     *
     * <ul>
     *     <li>200 OK: Listing OK.</li>
     *     <li>400 BAD REQUEST: No playerId specified.</li>
     *     <li>403 FORBIDDEN: The player does not own the game.</li>
     *     <li>404 NOT FOUND: The game or the player does not exist.</li>
     * </ul>
     *
     * * @param playerId The player Id obtained from registration.
     *
     * @return The list of games, sorted by creation timestamp.
     */
    @RequestMapping(method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ResponseBody
    public Game getPlayerGame(
            @PathVariable("gameId") String gameId,
            @RequestHeader(PlayersResource.PLAYER_ID_HEADER_NAME) @NotBlank String playerId) {
        return gameService.getPlayerGame(playerId, gameId);
    }

}
