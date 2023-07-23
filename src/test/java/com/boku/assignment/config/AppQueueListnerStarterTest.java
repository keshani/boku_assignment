package com.boku.assignment.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppQueueListnerStarterTest {

    @Autowired
    private AppQueueListnerStarter appQueueListnerStarter;

    @Test
    void test_listner_thread_is_running() {
        Thread listner = this.appQueueListnerStarter.getQueueListner();
        assertNotNull(listner);
        assertTrue(listner.isAlive());
    }
}