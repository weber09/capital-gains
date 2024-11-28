package org.capitalgains.model;

public class CapitalGainsContext {
    private double mediumPriceValue;
    private int stocksQuantity;
    private double lossValue;

    public double getMediumPriceValue() {
        return mediumPriceValue;
    }

    public int getStocksQuantity() {
        return stocksQuantity;
    }

    public double getLossValue() {
        return lossValue;
    }

    public void setLossValue(double lossValue) {
        this.lossValue = lossValue;
    }

    public void addLossValue(double lossValue) {
        this.lossValue += lossValue;
    }

    public void decreaseLossValue(double profit) {
        if (this.lossValue < profit) {
            this.lossValue = 0;
            return;
        }
        this.lossValue -= profit;
    }

    public void setMediumPriceValue(double mediumPriceValue) {
        this.mediumPriceValue = mediumPriceValue;
    }

    public void setStocksQuantity(int stocksQuantity) {
        this.stocksQuantity = stocksQuantity;
    }

    public void addStocksQuantity(int stocksQuantity) {
        this.stocksQuantity += stocksQuantity;
    }

    public void removeStocksQuantity(int stocksQuantity) {
        this.stocksQuantity -= stocksQuantity;
    }
}
