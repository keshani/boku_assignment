package com.boku.assignment.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppQueueListenerStarterTest {

    @Autowired
    private AppQueueListenerStarter appQueueListenerStarter;

    @Test
    void test_listener_thread_is_running() {
        Thread listener = this.appQueueListenerStarter.getQueueListener();
        assertNotNull(listener);
        assertTrue(listener.isAlive());
    }
}