package com.boku.assignment.message;

import com.boku.assignment.model.ClientNumberRequest;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class MessageQueueImpl implements MessageQueue {

    private static BlockingQueue<ClientNumberRequest> currentRequests = new LinkedBlockingQueue<>();
    private static BlockingQueue<ClientNumberRequest> historyRequests = new LinkedBlockingQueue<>();

    @Override
    public boolean sendMessage(ClientNumberRequest request) throws InterruptedException {
        currentRequests.put(request);
        return true;
    }

    @Override
    public ClientNumberRequest readMessageNonBlocking() throws InterruptedException {
        ClientNumberRequest request = currentRequests.poll();
        historyRequests.put(request);
        return request;
    }

    @Override
    public ClientNumberRequest readMessage() throws InterruptedException {
        ClientNumberRequest request = currentRequests.take();
        historyRequests.put(request);
        return request;

    }

}
