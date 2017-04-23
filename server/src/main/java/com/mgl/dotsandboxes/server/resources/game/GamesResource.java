package com.mgl.dotsandboxes.server.resources.game;

import com.google.common.base.Strings;
import com.mgl.dotsandboxes.server.resources.player.PlayersResource;
import com.mgl.dotsandboxes.server.model.game.Game;
import com.mgl.dotsandboxes.server.model.game.GameRequest;
import com.mgl.dotsandboxes.server.service.game.GameService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/games")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GamesResource {

    private final @NonNull GameService gameService;

    /**
     * Creates a new game, possibly as a follow up to a won game.
     *
     * @param asFollowUpTo Optionally specify which won game this new one is a follow-up for.
     * @param playerId The player Id obtained from registration.
     * @param gameRequest The parameters for the new game.
     *
     * Relevant HTTP status codes:
     *
     * <ul>
     *     <li>200 OK: Creation OK.</li>
     *     <li>400 BAD REQUEST: The provided game parameters (for example, dimension) are invalid
     *                    or no playerId specified.</li>
     *     <li>403 FORBIDDEN: The followed-up game (if any) does not belong to this player.</li>
     *     <li>404 NOT FOUND: The player does not exist
     *                    or the followed-up game (if any) does not exist.</li>
     *     <li>412 PRECONDITION FAILED: The followed-up game (if any) is not terminated
     *                    or was not won by the player.</li>
     * </ul>
     *
     * @return The new Game information and current state.
     */
    @RequestMapping(method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ResponseBody
    public @Valid Game createNewGame(
            @RequestParam(value = "asFollowUpTo", defaultValue = "") String asFollowUpTo,
            @RequestHeader(PlayersResource.PLAYER_ID_HEADER_NAME) @NotBlank String playerId,
            @RequestBody @Valid GameRequest gameRequest) {

        return gameService.createNewGame(
                playerId, gameRequest.getDimension(), gameRequest.getWhoStarts(),
                Optional.ofNullable(Strings.emptyToNull(asFollowUpTo)));
    }

    /**
     * Gets the list of games of a player (terminated or not).
     *
     * Relevant HTTP status codes:
     *
     * <ul>
     *     <li>200 OK: Listing OK.</li>
     *     <li>400 BAD REQUEST: No playerId specified.</li>
     *     <li>404 NOT FOUND: The player does not exist.</li>
     * </ul>
     *
     * * @param playerId The player Id obtained from registration.
     *
     * @return The list of games, sorted by creation timestamp.
     */
    @RequestMapping(method = RequestMethod.GET,
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ResponseBody
    public List<Game> getPlayerGames(
            @RequestHeader(PlayersResource.PLAYER_ID_HEADER_NAME) @NotBlank String playerId) {
        return gameService.getPlayerGames(playerId);
    }

}
