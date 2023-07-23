package com.boku.assignment.message;

import com.boku.assignment.model.ClientNumberRequest;
import org.springframework.stereotype.Component;

@Component
public interface MessageQueue {

    boolean sendMessage(ClientNumberRequest request) throws InterruptedException;

    ClientNumberRequest readMessage() throws InterruptedException;

    ClientNumberRequest readMessageNonBlocking() throws InterruptedException;
}
