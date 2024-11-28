package org.capitalgains.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MediumPriceCalculatorImplTest {

    private MediumPriceCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new MediumPriceCalculatorImpl();
    }

    @Test
    void shouldCalculateInitialMediumPrice() {
        // When buying first stocks, medium price should equal buy price
        double result = calculator.calculate(10.00, 0.0, 0, 100);
        assertEquals(10.00, result, 0.001);
    }

    @Test
    void shouldCalculateMediumPriceWithExistingStocks() {
        // Current: 10 stocks at $20.00
        // Buying: 5 stocks at $30.00
        // Expected: ((10 * 20.00) + (5 * 30.00)) / (10 + 5) = 23.33
        double result = calculator.calculate(30.00, 20.00, 10, 5);
        assertEquals(23.33, result, 0.01);
    }

    @Test
    void shouldCalculateMediumPriceWithLargeNumbers() {
        // Current: 10000 stocks at $15.00
        // Buying: 5000 stocks at $25.00
        double result = calculator.calculate(25.00, 15.00, 10000, 5000);
        assertEquals(18.33, result, 0.01);
    }

    @Test
    void shouldCalculateMediumPriceWithDecimalPrices() {
        // Current: 100 stocks at $10.55
        // Buying: 50 stocks at $15.75
        double result = calculator.calculate(15.75, 10.55, 100, 50);
        assertEquals(12.28, result, 0.01);
    }

    @Test
    void shouldHandleZeroQuantityPurchase() {
        // Should return current medium price when buying 0 stocks
        double currentMediumPrice = 10.00;
        double result = calculator.calculate(20.00, currentMediumPrice, 100, 0);
        assertEquals(currentMediumPrice, result, 0.001);
    }

    @Test
    void shouldHandleSamePriceScenario() {
        // When buying at same price, medium price should not change
        double price = 10.00;
        double result = calculator.calculate(price, price, 100, 50);
        assertEquals(price, result, 0.001);
    }
}

