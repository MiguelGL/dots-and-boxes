package com.mgl.dotsandboxes.server.resources.player;

import com.jayway.jsonpath.JsonPath;
import com.mgl.dotsandboxes.server.resources.support.BaseResourceIT;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PlayersResourceIT extends BaseResourceIT {

    @Test
    public void testValidPlayerRegistration() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/rest/players")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .content(mapper.createObjectNode()
                            .put("name", "Valid Name")
                            .put("email", "valid@email.com")
                            .toString()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.playerId", isA(String.class)))
                .andExpect(jsonPath("$.playerId", not(isEmptyOrNullString())));
    }

    @Test
    public void testValidRepeatedPlayerRegistration() throws Exception {
        String firstResponseContents = mockMvc.perform(MockMvcRequestBuilders
                .post("/rest/players")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.createObjectNode()
                        .put("name", "Valid Name")
                        .put("email", "valid@email.com")
                        .toString()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.playerId", isA(String.class)))
                .andExpect(jsonPath("$.playerId", not(isEmptyOrNullString())))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String firstPlayerId = JsonPath.parse(firstResponseContents).read("$.playerId");

        String secondResponseContents = mockMvc.perform(MockMvcRequestBuilders
                .post("/rest/players")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.createObjectNode()
                        .put("name", "Valid Name")
                        .put("email", "valid@email.com")
                        .toString()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.playerId", isA(String.class)))
                .andExpect(jsonPath("$.playerId", not(isEmptyOrNullString())))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String secondPlayerId = JsonPath.parse(secondResponseContents).read("$.playerId");

        Assert.assertEquals(firstPlayerId, secondPlayerId);
    }

    @Test
    public void testEmptyNamePlayerRegistrationError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/rest/players")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .content(mapper.createObjectNode()
                            .put("name", "  ") // Empty! (well, blank)
                            .put("email", "valid@email.com")
                            .toString()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testEmptyEmailPlayerRegistrationError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/rest/players")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .content(mapper.createObjectNode()
                            .put("name", "Valid Name")
                            .put("email", "") // Empty!
                            .toString()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testInvalidEmailPlayerRegistrationError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/rest/players")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .content(mapper.createObjectNode()
                            .put("name", "Valid Name")
                            .put("email", "noatsign.com") // Invalid!
                            .toString()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testMissingNamePlayerRegistrationError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/rest/players")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .content(mapper.createObjectNode()
                            // Missing! .put("name", "Valid Name")
                            .put("email", "valid@email.com")
                            .toString()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testMissingEmailPlayerRegistrationError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/rest/players")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaType.APPLICATION_JSON_UTF8)
                    .content(mapper.createObjectNode()
                            .put("name", "Valid Name")
                            // Missing! .put("email", "valid@email.com")
                            .toString()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

}
