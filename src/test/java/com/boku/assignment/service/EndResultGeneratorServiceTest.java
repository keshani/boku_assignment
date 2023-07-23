package com.boku.assignment.service;

import com.boku.assignment.model.ClientNumberRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Rollback
class EndResultGeneratorServiceTest {

    @Autowired
    private EndResultGeneratorService endResultGeneratorService;

    @Test
    void test_request_process_with_total_sum() {
        ClientNumberRequest req1 = new ClientNumberRequest("10");
        ClientNumberRequest req2 = new ClientNumberRequest("-12");
        ClientNumberRequest req3 = new ClientNumberRequest("14");
        ClientNumberRequest req4 = new ClientNumberRequest("end forwarding");

        endResultGeneratorService.processRequest(req1);
        endResultGeneratorService.processRequest(req2);
        endResultGeneratorService.processRequest(req3);

        assertEquals(12, endResultGeneratorService.getTotalSum());
        assertEquals(3, endResultGeneratorService.getProcessedMsg().size());

        endResultGeneratorService.processRequest(req4);
        assertEquals(0, endResultGeneratorService.getTotalSum());
        assertEquals(0, endResultGeneratorService.getProcessedMsg().size());
    }
}