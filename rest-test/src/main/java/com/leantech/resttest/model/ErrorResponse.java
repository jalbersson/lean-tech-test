package com.leantech.resttest.model;

import java.io.Serializable;

/**
 * This class is a model used as a response when an error occurs during the process of a Request
 */
public class ErrorResponse implements Serializable {
    private String errorMessage;

    public ErrorResponse() {
    }

    public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
