package com.boku.assignment.message;

import com.boku.assignment.model.ClientNumberRequest;
import com.boku.assignment.service.EndResultGeneratorService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Rollback
class MessageConsumerImplTest {

    @Mock
    private EndResultGeneratorService  endResultGeneratorService;

    @Mock
    private MessageQueueImpl messageQueue;

    @InjectMocks
    private MessageConsumerImpl messageConsumer;

    @Test
    public void testRun() {
        BlockingQueue<ClientNumberRequest> currentRequests = new LinkedBlockingQueue<>();
        ClientNumberRequest request1 = new ClientNumberRequest();
        ClientNumberRequest request2 = new ClientNumberRequest();
        try {
            currentRequests.put(request1);
            currentRequests.put(request2);

            Mockito.when(messageQueue.readMessage()).thenReturn(currentRequests.take()).thenReturn(currentRequests.take()).thenThrow(InterruptedException.class);
            Mockito.doNothing().when(endResultGeneratorService).processRequest(Mockito.any(ClientNumberRequest.class));

            Thread thread = new Thread(messageConsumer);
            thread.start();
            thread.join();
            assertTrue(currentRequests.isEmpty());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}