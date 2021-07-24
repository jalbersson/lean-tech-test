package com.leantech.resttest.model;

import java.io.Serializable;

/**
 * Used when creating a new Position record in database
 */
public class PositionRequest implements Serializable {
    private String positionName;

    public PositionRequest() {
    }

    public PositionRequest(String positionName) {
        this.positionName = positionName;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }
}
