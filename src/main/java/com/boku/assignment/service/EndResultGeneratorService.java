package com.boku.assignment.service;

import com.boku.assignment.message.MessageConsumerImpl;
import com.boku.assignment.model.ClientNumberRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.boku.assignment.common.util.GlobalConstants.END_INDICATOR;
import static com.boku.assignment.common.util.GlobalConstants.END_REQ_DIVIDER;

@Component
public class EndResultGeneratorService {

    Logger LOGGER = LoggerFactory.getLogger(EndResultGeneratorService.class);

    private static BlockingQueue<ClientNumberRequest> processedMsg = new LinkedBlockingQueue<>();

    private static Long totalSum = 0L;

    public void processRequest(ClientNumberRequest request) {
        try {
            processedMsg.put(request);
            if(request.getSendValue().startsWith(END_INDICATOR)) {
              publishEndResult(request);
            } else {
                calculateSum(request.getSendValue());
            }
        } catch (Exception ex) {
            LOGGER.error("EndResultGeneratorService::processRequest error occurred while processing request Error", ex);
            this.completeRequest(request, ex.getMessage());
        }
    }

    private void calculateSum(String value) {
        totalSum += Integer.valueOf(value);
    }

    private void publishEndResult(ClientNumberRequest request) {

        String endResultId = getEndResultId(request.getSendValue());
        String publishedValue = totalSum+" "+endResultId;

        while(!processedMsg.isEmpty()) {
            try {
                completeRequest(processedMsg.poll(),publishedValue);
            } catch (Exception ex) {
                LOGGER.error("EndResultGeneratorService::publishEndResult error occurred while processing request Error", ex);
            }
        }
        totalSum =0L;
    }

    private void completeRequest(ClientNumberRequest request, String endResult) {
        request.sendResponse(endResult);
    }

    private String getEndResultId(String endValue) {
        return endValue.split(END_REQ_DIVIDER)[1];
    }

    public static BlockingQueue<ClientNumberRequest> getProcessedMsg() {
        return processedMsg;
    }

    public Long getTotalSum() {
        return totalSum;
    }
}
