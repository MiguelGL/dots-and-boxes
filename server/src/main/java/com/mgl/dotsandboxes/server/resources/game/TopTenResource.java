package com.mgl.dotsandboxes.server.resources.game;

import com.mgl.dotsandboxes.server.model.game.ScoringSummary;
import com.mgl.dotsandboxes.server.service.game.ScoreService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/{playerId}/scores/top-ten")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))public class TopTenResource {

    private final @NonNull
    ScoreService scoreService;

    /**
     * Gets the top ten scores for a user.
     *
     * <ul>
     *     <li>200 OK: Result OK.</li>
     *     <li>404 NOT FOUND: Player not found.</li>
     * </ul>
     *
     * @param playerId The player Id obtained from registration.
     *
     * @return The list of the highest top ten scores.
     */
    @RequestMapping(method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ResponseBody
    public List<ScoringSummary> getTopTen(
            @PathVariable("playerId") String playerId) {
        return scoreService.calculateTopTen(playerId);
    }

}
