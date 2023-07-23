package com.boku.assignment.service;

import com.boku.assignment.model.ClientNumberRequest;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public interface RequestAccumulatorService {
    CompletableFuture<String> processClientRequest(String sendValue) throws InterruptedException;
}
