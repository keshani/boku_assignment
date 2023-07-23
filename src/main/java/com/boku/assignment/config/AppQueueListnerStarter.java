package com.boku.assignment.config;

import com.boku.assignment.controller.RequestAccumulator;
import com.boku.assignment.message.MessageConsumer;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Configuration
public class AppQueueListnerStarter {
    Logger LOGGER = LoggerFactory.getLogger(AppQueueListnerStarter.class);

    private final Thread queueListner;
    private MessageConsumer messageConsumer;

    public Thread getQueueListner() {
        return queueListner;
    }

    @Autowired
    public AppQueueListnerStarter(MessageConsumer messageConsumer) {
        this.messageConsumer = messageConsumer;
        this.queueListner = new Thread(messageConsumer);
    }

    @PostConstruct
    public void startThread() {
        LOGGER.info("Application Queue listner thread started");
        try {
            queueListner.start();
        } catch (Exception ex) {
            LOGGER.error("Application Queue listner thread failed to start",ex);
        }
    }
}
