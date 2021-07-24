package com.leantech.resttest.model;

import java.io.Serializable;

public class SuccessfulResponse implements Serializable {
    private String message;

    public SuccessfulResponse() {
    }

    public SuccessfulResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
