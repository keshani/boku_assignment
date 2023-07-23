package com.boku.assignment.message;

import com.boku.assignment.model.ClientNumberRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Rollback
class MessageProducerImplTest {

    @Autowired
    private MessageQueueImpl messageQueue;

    @Autowired
    private MessageProducerImpl messageProducer;

    @Test
    void test_msg_send() {
        ClientNumberRequest request = new ClientNumberRequest("5");
        try {
            messageProducer.send(request);
            ClientNumberRequest result =  messageQueue.readMessageNonBlocking();
            assertEquals(request.getId(), result.getId());
            assertEquals("5", result.getSendValue());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}