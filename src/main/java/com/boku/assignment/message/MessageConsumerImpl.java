package com.boku.assignment.message;

import com.boku.assignment.controller.RequestAccumulator;
import com.boku.assignment.model.ClientNumberRequest;
import com.boku.assignment.service.EndResultGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumerImpl implements MessageConsumer {
    Logger LOGGER = LoggerFactory.getLogger(MessageConsumerImpl.class);
    private MessageQueue messageQueue;
    private EndResultGeneratorService endResultGenerator;

    @Autowired
    public MessageConsumerImpl(MessageQueue messageQueue, EndResultGeneratorService endResultGenerator) {
        this.messageQueue = messageQueue;
        this.endResultGenerator = endResultGenerator;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // poll the queue and process each message
                ClientNumberRequest request = this.messageQueue.readMessage();
                this.endResultGenerator.processRequest(request);
            }
        } catch (InterruptedException ex) {
            LOGGER.error("MessageConsumerImpl:run error occurred while reading the queue",ex);
            Thread.currentThread().interrupt();
            return;
        }

    }
}
