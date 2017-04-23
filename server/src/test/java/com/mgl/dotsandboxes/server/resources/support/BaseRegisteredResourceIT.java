package com.mgl.dotsandboxes.server.resources.support;

import com.jayway.jsonpath.JsonPath;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Random;

import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public abstract class BaseRegisteredResourceIT extends BaseResourceIT {

    protected static Random random;

    protected int rnd;
    protected String playerId;

    @BeforeClass
    public static void setUpClass() {
        BaseResourceIT.setUpClass();
        random = new Random();
    }

    @Before
    public void setUp() throws Exception {
        rnd = random.nextInt(10_000);
        playerId = registerUser(String.valueOf(rnd));
    }

    @After
    public void tearDown() {
        playerId = null;
    }

    @AfterClass
    public static void tearDownClass() {
        random = null;
    }

    protected String registerUser(String prefix) throws Exception {
        String responseContents = mockMvc.perform(MockMvcRequestBuilders
                .post("/rest/players")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content(mapper.createObjectNode()
                        .put("name", prefix + " Name")
                        .put("email", prefix + "@email.com")
                        .toString()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.playerId", isA(String.class)))
                .andExpect(jsonPath("$.playerId", not(isEmptyOrNullString())))
                .andReturn()
                .getResponse()
                .getContentAsString();

        return JsonPath.parse(responseContents).read("$.playerId");
    }

}
