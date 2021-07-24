package com.leantech.resttest.model;

/**
 * This is a simple model used as a request when trying to find a record by its ID
 */
public class IdRequest {
    private Long id;

    public IdRequest() {
    }

    public IdRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
