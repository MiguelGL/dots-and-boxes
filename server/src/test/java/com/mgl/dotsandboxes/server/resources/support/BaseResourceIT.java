package com.mgl.dotsandboxes.server.resources.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

public abstract class BaseResourceIT {

    protected static ObjectMapper mapper;

    @Autowired
    protected MockMvc mockMvc;

    @BeforeClass
    public static void setUpClass() {
        mapper = new ObjectMapper();
    }

    @AfterClass
    public static void tearDownClass() {
        mapper = null;
    }

}
