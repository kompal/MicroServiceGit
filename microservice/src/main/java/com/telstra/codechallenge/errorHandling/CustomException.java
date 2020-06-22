package com.telstra.codechallenge.errorHandling;

public class CustomException extends RuntimeException {
    private String message;
    private String details;
    private Integer statusCode;

    protected CustomException() {
    }

    public CustomException(
            String message, String details, Integer statusCode) {
        this.message = message;
        this.details = details;
        this.statusCode = statusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
}