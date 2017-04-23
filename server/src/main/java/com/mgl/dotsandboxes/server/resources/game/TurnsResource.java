package com.mgl.dotsandboxes.server.resources.game;

import com.mgl.dotsandboxes.server.resources.player.PlayersResource;
import com.mgl.dotsandboxes.server.model.game.Connection;
import com.mgl.dotsandboxes.server.model.game.GameSnapshot;
import com.mgl.dotsandboxes.server.service.game.GameService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/rest/games/{gameId}/turns")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TurnsResource {

    private final @NonNull GameService gameService;

    /**
     * Plays a new turn ("move").
     *
     * Relevant HTTP status codes:
     *
     * <ul>
     *     <li>200 OK: Turn OK.</li>
     *     <li>400 BAD REQUEST: No playerId specified or invalid connection
     *              (out of bounds, not adjacent dots).</li>
     *     <li>403 FORBIDDEN: The player does not own the game.</li>
     *     <li>404 NOT FOUND: The game or the player does not exist.</li>
     *     <li>409 CONFLICT: The connection has already been made.</li>
     *     <li>412 PRECONDITION FAILED: The game is already terminated.</li>
     * </ul>
     *
     * @param gameId The Id of the Game to play.
     * @param playerId The player Id obtained from registration.
     * @param connection The turn ("move") to make.
     *
     * @return The resulting game snapshot.
     */
    @RequestMapping(method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ResponseBody
    public @Valid GameSnapshot playTurn(
            @PathVariable("gameId") String gameId,
            @RequestHeader(PlayersResource.PLAYER_ID_HEADER_NAME) @NotBlank String playerId,
            @RequestBody @Valid Connection connection) {
        return gameService.playHumanTurn(playerId, gameId, connection);
    }

}
