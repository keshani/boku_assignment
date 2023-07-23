package com.boku.assignment.service;

import com.boku.assignment.message.MessageProducer;
import com.boku.assignment.model.ClientNumberRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class RequestAccumulatorServiceImpl implements RequestAccumulatorService{

    Logger LOGGER = LoggerFactory.getLogger(EndResultGeneratorService.class);
    private MessageProducer messageProducer;

    @Autowired
    public RequestAccumulatorServiceImpl(MessageProducer messageProducer) {
        this.messageProducer = messageProducer;
    }

    @Override
    public CompletableFuture<String> processClientRequest(String sendValue) throws InterruptedException {
        // validate input value
        validateInputValue(sendValue);

        ClientNumberRequest req = new ClientNumberRequest(refineString(sendValue));
        messageProducer.send(req);

        return req.getResponse();
    }

    private String refineString(String sendValue) {
        return sendValue.replace("+", " ").replaceAll("=", "");
    }

    private void validateInputValue(String sendValue) {
        if(sendValue == null || sendValue.isBlank()) {
            throw new IllegalArgumentException("Send parameter value cannot be empty or null");
        }
        sendValue = sendValue.replace("+", " ").replace("=", "");

        if(!sendValue.matches("^(-?\\d+|end \\S+)$")) {
            throw new IllegalArgumentException("Send parameter value has to be number or string which start" +
                    " with \"end \" followed by space and the another string ");
        }
    }

}
