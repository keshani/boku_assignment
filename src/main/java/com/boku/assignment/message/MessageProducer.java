package com.boku.assignment.message;

import com.boku.assignment.model.ClientNumberRequest;
import org.springframework.stereotype.Component;

@Component
public interface MessageProducer {
    void send(ClientNumberRequest request) throws InterruptedException;
}
