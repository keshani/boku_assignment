package com.boku.assignment.message;

import com.boku.assignment.model.ClientNumberRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageProducerImpl implements MessageProducer {
    private MessageQueue messageQueue;

    @Autowired
    public MessageProducerImpl(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    @Override
    public void send(ClientNumberRequest request) throws InterruptedException {
        this.messageQueue.sendMessage(request);
    }

}
