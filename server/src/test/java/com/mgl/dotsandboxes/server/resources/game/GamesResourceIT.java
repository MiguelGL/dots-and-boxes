package com.mgl.dotsandboxes.server.resources.game;

import com.jayway.jsonpath.JsonPath;
import com.mgl.dotsandboxes.server.resources.player.PlayersResource;
import com.mgl.dotsandboxes.server.resources.support.BaseRegisteredResourceIT;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GamesResourceIT extends BaseRegisteredResourceIT {

    protected static String createGame(
            MockMvc mockMvc, String playerId, int dim, String whoStarts) throws Exception {
        String responseContents = mockMvc.perform(MockMvcRequestBuilders
                .post("/rest/games")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .header(PlayersResource.PLAYER_ID_HEADER_NAME, playerId)
                .content(mapper.createObjectNode()
                        .put("dimension", dim)
                        .put("whoStarts", whoStarts)
                        .toString()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn()
                .getResponse()
                .getContentAsString();

        return JsonPath.parse(responseContents).read("$.gameId");
    }

    private String createGame(String playerId, int dim, String whoStarts) throws Exception {
        return createGame(mockMvc, playerId, dim, whoStarts);
    }

    @Test
    public void testCreateValidHumanStartedGame() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/rest/games")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .header(PlayersResource.PLAYER_ID_HEADER_NAME, playerId)
                    .content(mapper.createObjectNode()
                        .put("dimension", 3)
                        .put("whoStarts", "HUMAN")
                        .toString()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.gameId", isA(String.class)))
                .andExpect(jsonPath("$.gameId", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.playerId", isA(String.class)))
                .andExpect(jsonPath("$.playerId", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.creationTs", isA(Long.class)))
                .andExpect(jsonPath("$.dimension", is(3)))
                .andExpect(jsonPath("$.currentSnapshot.state", is("CREATED")))
                .andExpect(jsonPath("$.currentSnapshot.turnResults", empty()))
                .andExpect(jsonPath("$.currentSnapshot.winner", Matchers.nullValue()))
                .andExpect(jsonPath("$.currentSnapshot.score", is(0)));
    }

    @Test
    public void testCreateValidComputerStartedGame() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/rest/games")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .header(PlayersResource.PLAYER_ID_HEADER_NAME, playerId)
                    .content(mapper.createObjectNode()
                        .put("dimension", 4)
                        .put("whoStarts", "COMPUTER")
                        .toString()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.gameId", isA(String.class)))
                .andExpect(jsonPath("$.gameId", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.playerId", isA(String.class)))
                .andExpect(jsonPath("$.playerId", not(isEmptyOrNullString())))
                .andExpect(jsonPath("$.creationTs", isA(Long.class)))
                .andExpect(jsonPath("$.dimension", is(4)))
                .andExpect(jsonPath("$.currentSnapshot.state", is("ONGOING")))
                .andExpect(jsonPath("$.currentSnapshot.turnResults", hasSize(1)))
                .andExpect(jsonPath("$.currentSnapshot.winner", Matchers.nullValue()))
                .andExpect(jsonPath("$.currentSnapshot.score", is(0)))
                .andExpect(jsonPath("$.currentSnapshot.turnResults[0].playerKind", is("COMPUTER")))
                .andExpect(jsonPath("$.currentSnapshot.turnResults[0].ts", isA(Long.class)))
                .andExpect(jsonPath("$.currentSnapshot.turnResults[0].boxes", empty()))
                .andExpect(jsonPath("$.currentSnapshot.turnResults[0].moveId", is(1)))
                .andExpect(jsonPath("$.currentSnapshot.turnResults[0].resultingBoard.dimension", is(4)))
                .andExpect(jsonPath("$.currentSnapshot.turnResults[0].resultingBoard.hzConnections", hasSize(5)))
                .andExpect(jsonPath("$.currentSnapshot.turnResults[0].resultingBoard.vtConnections", hasSize(5)))
                .andExpect(jsonPath("$.currentSnapshot.turnResults[0].resultingBoard.hzConnections[0]", hasSize(4)))
                .andExpect(jsonPath("$.currentSnapshot.turnResults[0].resultingBoard.vtConnections[0]", hasSize(4)));
    }

    @Test
    public void testCreateInvalidDimensionGameError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/rest/games")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .header(PlayersResource.PLAYER_ID_HEADER_NAME, playerId)
                .content(mapper.createObjectNode()
                        .put("dimension", 1) // Too small!
                        .put("whoStarts", "HUMAN")
                        .toString()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateGameSpecifiedPlayerIdNotFoundError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/rest/games")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .header(PlayersResource.PLAYER_ID_HEADER_NAME, "aplayeridimadeup") // Fake!
                .content(mapper.createObjectNode()
                        .put("dimension", 2)
                        .put("whoStarts", "HUMAN")
                        .toString()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateGameSpecifiedFollowUpGameIdNotFoundError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/rest/games")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .header(PlayersResource.PLAYER_ID_HEADER_NAME, playerId)
                .param("asFollowUpTo", "gameidthatdoesnotexist")
                .content(mapper.createObjectNode()
                        .put("dimension", 2)
                        .put("whoStarts", "HUMAN")
                        .toString()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateGameNoPlayerIdError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/rest/games")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                // Missing! .header(PLAYER_ID_HEADER_NAME, playerId)
                .content(mapper.createObjectNode()
                        .put("dimension", 2)
                        .put("whoStarts", "HUMAN")
                        .toString()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateGameFollowUpToNotOwnedGameError() throws Exception {
        String auxPlayerId = registerUser("aux-" + rnd);
        String gameId = createGame(auxPlayerId, 3, "HUMAN");

        mockMvc.perform(MockMvcRequestBuilders
                .post("/rest/games")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .header(PlayersResource.PLAYER_ID_HEADER_NAME, playerId)
                .param("asFollowUpTo", gameId)
                .content(mapper.createObjectNode()
                        .put("dimension", 3)
                        .put("whoStarts", "HUMAN")
                        .toString()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden());
    }

    @Test @Ignore
    public void testCreateGameFollowUpToNonTerminatedGame() throws Exception {
        // TODO: Prepare test code to execute a game play so that it gets terminated.
        Assert.fail("Not yet implemented");
    }

    @Test @Ignore
    public void testCreateGameFollowUpToNonWonGame() throws Exception {
        // TODO: Prepare test code to execute a game play so that it gets won by the player.
        Assert.fail("Not yet implemented");
    }

    @Test
    public void testGetPlayerGames() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/rest/games")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .header(PlayersResource.PLAYER_ID_HEADER_NAME, playerId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", empty()));

        createGame(playerId, 3, "HUMAN");
        createGame(playerId, 4, "COMPUTER");

        mockMvc.perform(MockMvcRequestBuilders
                .get("/rest/games")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .header(PlayersResource.PLAYER_ID_HEADER_NAME, playerId))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$[*]", hasSize(2)));
    }

}
