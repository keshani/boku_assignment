package com.boku.assignment.config;

import com.boku.assignment.message.MessageConsumer;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppQueueListenerStarter {
    Logger LOGGER = LoggerFactory.getLogger(AppQueueListenerStarter.class);

    private final Thread queueListener;
    private MessageConsumer messageConsumer;

    public Thread getQueueListener() {
        return queueListener;
    }

    @Autowired
    public AppQueueListenerStarter(MessageConsumer messageConsumer) {
        this.messageConsumer = messageConsumer;
        this.queueListener = new Thread(messageConsumer);
    }

    @PostConstruct
    public void startThread() {
        LOGGER.info("Application Queue listener thread started");
        try {
            queueListener.start();
        } catch (Exception ex) {
            LOGGER.error("Application Queue listener thread failed to start",ex);
        }
    }
}
