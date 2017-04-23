package com.mgl.dotsandboxes.server.resources.game;

import com.mgl.dotsandboxes.server.resources.support.BaseRegisteredResourceIT;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static com.mgl.dotsandboxes.server.resources.player.PlayersResource.PLAYER_ID_HEADER_NAME;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GameResourceIT extends BaseRegisteredResourceIT {

    @Test
    public void testGetPlayerGame() throws Exception {
        String gameId = GamesResourceIT.createGame(mockMvc, playerId, 3, "HUMAN");

        mockMvc.perform(MockMvcRequestBuilders
                .get("/rest/games/{gameId}", gameId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .header(PLAYER_ID_HEADER_NAME, playerId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }

    @Test
    public void testGetMissingPlayerGame() throws Exception {
        String gameId = GamesResourceIT.createGame(mockMvc, playerId, 3, "HUMAN");

        mockMvc.perform(MockMvcRequestBuilders
                .get("/rest/games/{gameId}", gameId)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                // Missing! .header(PLAYER_ID_HEADER_NAME, playerId)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetNonExistingPlayerGame() throws Exception {
        String gameId = GamesResourceIT.createGame(mockMvc, playerId, 3, "HUMAN");

        mockMvc.perform(MockMvcRequestBuilders
                .get("/rest/games/{gameId}", gameId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .header(PLAYER_ID_HEADER_NAME, "nonexistingplayer"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetNotOwnedPlayerGame() throws Exception {
        String auxPlayerId = registerUser("aux" + rnd);
        String gameId = GamesResourceIT.createGame(mockMvc, auxPlayerId, 4, "COMPUTER");

        mockMvc.perform(MockMvcRequestBuilders
                .get("/rest/games/{gameId}", gameId)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .header(PLAYER_ID_HEADER_NAME, playerId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetPlayerNonExistingGame() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/rest/games/{gameId}", "nonexistinggameid")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .header(PLAYER_ID_HEADER_NAME, playerId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

}
