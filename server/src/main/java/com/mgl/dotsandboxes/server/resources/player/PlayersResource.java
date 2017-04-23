package com.mgl.dotsandboxes.server.resources.player;

import com.mgl.dotsandboxes.server.model.player.PlayerRegistration;
import com.mgl.dotsandboxes.server.repository.player.PlayerRepository;
import com.mgl.dotsandboxes.server.model.player.PlayerProfile;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/rest/players")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PlayersResource {

    public static final String PLAYER_ID_HEADER_NAME = "X-Player-Id";

    private final @NonNull
    PlayerRepository playerRepository;

    /**
     * Registers a player for playing the game.
     *
     * Returns a registration information including the <code>playerId</code>
     * that must be included as a header for further player-specific actions.
     *
     * The key for the registration is the provided email address. When registering an email
     * that has already registered, the existing <code>playerId</code> is returned.
     *
     * Relevant HTTP status codes:
     *
     * <ul>
     *     <li>200 OK: Registration OK. Also for repeated registrations.</li>
     *     <li>400 BAD REQUEST: The provided player profile is invalid.</li>
     * </ul>
     *
     * @param playerProfile The player profile information.
     *
     * @return The player registration information.
     */
    @RequestMapping(method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ResponseBody
    public @Valid
    PlayerRegistration registerPlayer(
            @RequestBody @Valid PlayerProfile playerProfile) {
        return playerRepository.ensurePlayerRegistered(playerProfile);
    }

}
