package com.leantech.resttest.model;

import com.leantech.resttest.entity.Position;

import java.io.Serializable;

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
