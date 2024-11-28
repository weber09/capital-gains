package org.capitalgains.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Operation {
    private OperationType operationType;
    @JsonProperty("unit-cost")
    private double unitCost;
    private int quantity;

    public OperationType getOperationType() {
        return operationType;
    }

    @JsonProperty("operation")
    public void setOperationType(String operationType) {
        this.operationType = OperationType.valueOf(operationType.toUpperCase());
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(double unitCost) {
        this.unitCost = unitCost;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
