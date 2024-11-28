package org.capitalgains.service;

public interface TaxCalculator {
    double calculateTax(double price, double mediumPrice, double lossValue, int quantity);
}
