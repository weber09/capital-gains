package org.capitalgains.service;

public class MediumPriceCalculatorImpl implements MediumPriceCalculator{
    @Override
    public double calculate(double buyPrice, double currentMediumPriceValue, int currentStocksQuantity, int stocksBoughtQuantity) {
        return ((currentMediumPriceValue * currentStocksQuantity)
                + (buyPrice * stocksBoughtQuantity))
                / (currentStocksQuantity + stocksBoughtQuantity);
    }
}
