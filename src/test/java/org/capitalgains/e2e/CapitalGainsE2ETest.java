package org.capitalgains.e2e;

import org.capitalgains.handler.CapitalGainsTaxCalculatorHandler;
import org.capitalgains.handler.CapitalGainsTaxCalculatorHandlerImpl;
import org.capitalgains.model.Operation;
import org.capitalgains.model.OperationType;
import org.capitalgains.model.Tax;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Arrays;

class CapitalGainsE2ETest {

    @Test
    void shouldCalculateCapitalGainsForCompleteScenario() {
        // Given
        CapitalGainsTaxCalculatorHandler handler = new CapitalGainsTaxCalculatorHandlerImpl(); // No mocks, real implementation

        List<Operation> operations = Arrays.asList(
                createOperation(OperationType.BUY, 10000, 10.00),
                createOperation(OperationType.SELL, 10000, 50.00),
                createOperation(OperationType.BUY, 10000, 20.00),
                createOperation(OperationType.SELL, 10000, 50.00)
        );

        // When
        List<Tax> taxes = handler.calculateTaxes(operations);

        // Then
        assertEquals(4, taxes.size());
        assertEquals(0.0, taxes.get(0).getTax()); // First buy - no tax
        assertEquals(80000.0, taxes.get(1).getTax()); // First sell - (50-10) * 10000 * 0.2
        assertEquals(0.0, taxes.get(2).getTax()); // Second buy - no tax
        assertEquals(60000.0, taxes.get(3).getTax()); // Second sell - (50-20) * 10000 * 0.2
    }

    @Test
    void shouldHandleComplexScenarioWithLosses() {
        // Given
        CapitalGainsTaxCalculatorHandler handler = new CapitalGainsTaxCalculatorHandlerImpl();

        List<Operation> operations = Arrays.asList(
                // Initial purchase
                createOperation(OperationType.BUY, 1000, 20.00), //20 qt 1000
                // Sell at loss
                createOperation(OperationType.SELL, 500, 10.00), //20 qt 500 loss 5000
                // Buy more
                createOperation(OperationType.BUY, 500, 15.00), //27.5 qt 1000
                // Sell with profit but previous loss should be considered
                createOperation(OperationType.SELL, 1000, 25.00)
        );

        // When
        List<Tax> taxes = handler.calculateTaxes(operations);

        // Then
        assertEquals(4, taxes.size());
        assertEquals(0.0, taxes.get(0).getTax()); // Buy - no tax
        assertEquals(0.0, taxes.get(1).getTax()); // Sell at loss - no tax
        assertEquals(0.0, taxes.get(2).getTax()); // Buy - no tax
        // Last sell - should consider previous loss
        //last medium price: 17.50 previous loss: 5000
        //expected tax: 500
        assertEquals(500, taxes.get(3).getTax(), 0.01);
    }

    @Test
    void shouldHandleOperationsWithMinimumTaxableAmount() {
        // Given
        CapitalGainsTaxCalculatorHandler handler = new CapitalGainsTaxCalculatorHandlerImpl();

        List<Operation> operations = Arrays.asList(
                createOperation(OperationType.BUY, 100, 10.00),
                createOperation(OperationType.SELL, 100, 15.00) // Small operation below tax threshold
        );

        // When
        List<Tax> taxes = handler.calculateTaxes(operations);

        // Then
        assertEquals(2, taxes.size());
        assertEquals(0.0, taxes.get(0).getTax());
        assertEquals(0.0, taxes.get(1).getTax()); // Should be 0 due to minimum taxable amount
    }

    @Test
    void shouldHandleMultipleOperationsWithWeightedAveragePriceCalculation() {
        // Given
        CapitalGainsTaxCalculatorHandler handler = new CapitalGainsTaxCalculatorHandlerImpl();

        List<Operation> operations = Arrays.asList(
                createOperation(OperationType.BUY, 1000, 10.00),
                createOperation(OperationType.BUY, 1000, 15.00),
                createOperation(OperationType.SELL, 1500, 20.00)
        );

        // When
        List<Tax> taxes = handler.calculateTaxes(operations);

        // Then
        assertEquals(3, taxes.size());
        assertEquals(0.0, taxes.get(0).getTax());
        assertEquals(0.0, taxes.get(1).getTax());
        // Weighted average price should be (1000*10 + 1000*15)/(2000) = 12.50
        double expectedTax = (20.00 - 12.50) * 1500 * 0.2;
        assertEquals(expectedTax, taxes.get(2).getTax(), 0.01);
    }

    private Operation createOperation(OperationType type, int quantity, double unitCost) {
        Operation operation = new Operation();
        operation.setOperationType(type);
        operation.setQuantity(quantity);
        operation.setUnitCost(unitCost);
        return operation;
    }
}

