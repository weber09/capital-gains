package org.capitalgains.service;

public interface MediumPriceCalculator {
    double calculate(double buyPrice, double currentMediumPriceValue, int currentStocksQuantity, int stocksBoughtQuantity);
}
