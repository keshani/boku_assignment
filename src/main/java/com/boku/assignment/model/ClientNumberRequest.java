package com.boku.assignment.model;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ClientNumberRequest {

    private UUID id;
    private String sendValue;
    private CompletableFuture<String> response;

    public ClientNumberRequest() {
        this.response = new CompletableFuture<String>();
        this.id = UUID.randomUUID();
    }

    public ClientNumberRequest(String sendValue) {
        this.sendValue = sendValue;
        this.response = new CompletableFuture<String>();
        this.id = UUID.randomUUID();
    }

    public String getSendValue() {
        return sendValue;
    }

    public void setSendValue(String sendValue) {
        this.sendValue = sendValue;
    }

    public CompletableFuture<String> getResponse() {
        return response;
    }

    public void setResponse(CompletableFuture<String> response) {
        this.response = response;
    }

    public void sendResponse(String endResultValue) {
        this.response.complete(endResultValue);
    }

    public UUID getId() {
        return id;
    }

}
