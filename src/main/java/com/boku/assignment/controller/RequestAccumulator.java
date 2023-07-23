package com.boku.assignment.controller;

import com.boku.assignment.model.ClientNumberRequest;
import com.boku.assignment.message.MessageProducer;
import com.boku.assignment.service.RequestAccumulatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/boku/accumulator")
public class RequestAccumulator {

    Logger LOGGER = LoggerFactory.getLogger(RequestAccumulator.class);

    private RequestAccumulatorService requestAccumulatorService;

    @Autowired
    public RequestAccumulator(RequestAccumulatorService requestAccumulatorService) {
        this.requestAccumulatorService = requestAccumulatorService;
    }

    @PostMapping("/process_requests")
    public CompletableFuture<String> accumulatRequest(@RequestBody String param) {
        LOGGER.info("Client request received with param = ", param);
        CompletableFuture<String> result = null;
        try {
            result = this.requestAccumulatorService.processClientRequest(param);
        } catch (Exception ex) {
            LOGGER.error("RequestAccumulator::accumulatRequest error ocuured :" , ex);
            if(result == null) {
                result = new CompletableFuture<String>();
            }
            result.completeExceptionally(ex);
        }
        return result;
    }

}
