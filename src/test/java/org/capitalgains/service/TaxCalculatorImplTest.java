package org.capitalgains.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaxCalculatorImplTest {

    private TaxCalculator calculator;
    private TaxCalculator calculatorCustomValues;

    @BeforeEach
    void setUp() {
        // Default constructor uses 0.2 tax rate and 20000 minimum cost
        calculator = new TaxCalculatorImpl();
        // Custom constructor with different tax rate and minimum cost
        calculatorCustomValues = new TaxCalculatorImpl(0.15, 10000);
    }

    @Test
    void shouldReturnZeroTaxWhenOperationBellowMinimumCost() {
        // Given: Operation total cost below 20000
        double tax = calculator.calculateTax(10.0, 15.0, 0.0, 1000);
        assertEquals(0.0, tax, 0.001);
    }

    @Test
    void shouldReturnZeroTaxWhenOperationIsLoss() {
        // Given: Selling price lower than medium price
        double tax = calculator.calculateTax(10.0, 15.0, 0.0, 3000);
        assertEquals(0.0, tax, 0.001);
    }

    @Test
    void shouldCalculateTaxForProfitOperation() {
        // Given: Selling price higher than medium price
        // Total operation: 25.0 * 1000 = 25000 (above minimum)
        // Profit per unit: 25.0 - 20.0 = 5.0
        // Total profit: 5.0 * 1000 = 5000
        // Tax: 5000 * 0.2 = 1000
        double tax = calculator.calculateTax(25.0, 20.0, 0.0, 1000);
        assertEquals(1000.0, tax, 0.001);
    }

    @Test
    void shouldConsiderLossValueInTaxCalculation() {
        // Given: Previous loss of 2000
        // Total operation: 30.0 * 1000 = 30000 (above minimum)
        // Profit per unit: 30.0 - 20.0 = 10.0
        // Total profit: 10.0 * 1000 = 10000
        // Profit after loss: 10000 - 2000 = 8000
        // Tax: 8000 * 0.2 = 1600
        double tax = calculator.calculateTax(30.0, 20.0, 2000.0, 1000);
        assertEquals(1600.0, tax, 0.001);
    }

    @Test
    void shouldReturnZeroWhenLossValueExceedsProfit() {
        // Given: Loss value higher than potential profit
        // Total operation: 25.0 * 1000 = 25000 (above minimum)
        // Profit per unit: 25.0 - 20.0 = 5.0
        // Total profit: 5.0 * 1000 = 5000
        // Previous loss: 6000 (exceeds profit)
        double tax = calculator.calculateTax(25.0, 20.0, 6000.0, 1000);
        assertEquals(0.0, tax, 0.001);
    }

    @Test
    void shouldCalculateTaxWithCustomTaxRate() {
        // Given: Custom tax rate of 15%
        // Total operation: 30.0 * 1000 = 30000 (above minimum)
        // Profit per unit: 30.0 - 20.0 = 10.0
        // Total profit: 10.0 * 1000 = 10000
        // Tax: 10000 * 0.15 = 1500
        double tax = calculatorCustomValues.calculateTax(30.0, 20.0, 0.0, 1000);
        assertEquals(1500.0, tax, 0.001);
    }

    @Test
    void shouldCalculateTaxWithCustomMinimumCost() {
        // Given: Custom minimum cost of 10000
        // Total operation: 15.0 * 1000 = 15000 (above custom minimum)
        // Profit per unit: 15.0 - 10.0 = 5.0
        // Total profit: 5.0 * 1000 = 5000
        // Tax: 5000 * 0.15 = 750
        double tax = calculatorCustomValues.calculateTax(15.0, 10.0, 0.0, 1000);
        assertEquals(750.0, tax, 0.001);
    }

    @Test
    void shouldHandleZeroQuantity() {
        double tax = calculator.calculateTax(25.0, 20.0, 0.0, 0);
        assertEquals(0.0, tax, 0.001);
    }

    @Test
    void shouldHandleDecimalPrices() {
        // Total operation: 25.75 * 1000 = 25750 (above minimum)
        // Profit per unit: 25.75 - 20.25 = 5.50
        // Total profit: 5.50 * 1000 = 5500
        // Tax: 5500 * 0.2 = 1100
        double tax = calculator.calculateTax(25.75, 20.25, 0.0, 1000);
        assertEquals(1100.0, tax, 0.001);
    }

    @Test
    void shouldHandleLargeNumbers() {
        // Total operation: 30.0 * 10000 = 300000 (above minimum)
        // Profit per unit: 30.0 - 25.0 = 5.0
        // Total profit: 5.0 * 10000 = 50000
        // Tax: 50000 * 0.2 = 10000
        double tax = calculator.calculateTax(30.0, 25.0, 0.0, 10000);
        assertEquals(10000.0, tax, 0.001);
    }
}
