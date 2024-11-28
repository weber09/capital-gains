package org.capitalgains.handler;

import org.capitalgains.model.CapitalGainsContext;
import org.capitalgains.model.Operation;
import org.capitalgains.model.OperationType;
import org.capitalgains.model.Tax;
import org.capitalgains.service.MediumPriceCalculator;
import org.capitalgains.service.TaxCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CapitalGainsTaxCalculatorHandlerImplTest {

    private CapitalGainsTaxCalculatorHandlerImpl handler;

    @Mock
    private MediumPriceCalculator mediumPriceCalculator;

    @Mock
    private TaxCalculator taxCalculator;

    @Mock
    private BuyOperationHandler buyOperationHandler;

    @Mock
    private SellOperationHandler sellOperationHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        handler = new CapitalGainsTaxCalculatorHandlerImpl(
                mediumPriceCalculator,
                taxCalculator,
                buyOperationHandler,
                sellOperationHandler
        );
    }

    @Test
    void shouldHandleSingleBuyOperation() {
        // Given
        Operation buyOp = new Operation();
        buyOp.setOperationType(OperationType.BUY);
        buyOp.setQuantity(100);
        buyOp.setUnitCost(10.0);

        List<Operation> operations = List.of(buyOp);

        // When
        List<Tax> result = handler.calculateTaxes(operations);

        // Then
        verify(buyOperationHandler).handle(eq(buyOp), any(CapitalGainsContext.class));
        assertEquals(1, result.size());
    }

    @Test
    void shouldHandleSingleSellOperation() {
        // Given
        Operation sellOp = new Operation();
        sellOp.setOperationType(OperationType.SELL);
        sellOp.setQuantity(50);
        sellOp.setUnitCost(15.0);

        List<Operation> operations = List.of(sellOp);

        // When
        List<Tax> result = handler.calculateTaxes(operations);

        // Then
        verify(sellOperationHandler).handle(eq(sellOp), any(CapitalGainsContext.class));
        assertEquals(1, result.size());
    }

    @Test
    void shouldHandleMultipleOperations() {
        // Given
        Operation buyOp = new Operation();
        buyOp.setOperationType(OperationType.BUY);
        buyOp.setQuantity(100);
        buyOp.setUnitCost(10.0);

        Operation sellOp = new Operation();
        sellOp.setOperationType(OperationType.SELL);
        sellOp.setQuantity(50);
        sellOp.setUnitCost(15.0);

        List<Operation> operations = Arrays.asList(buyOp, sellOp);

        // When
        List<Tax> result = handler.calculateTaxes(operations);

        // Then
        verify(buyOperationHandler).handle(eq(buyOp), any(CapitalGainsContext.class));
        verify(sellOperationHandler).handle(eq(sellOp), any(CapitalGainsContext.class));
        assertEquals(2, result.size());
    }

    @Test
    void shouldHandleEmptyOperationsList() {
        // Given
        List<Operation> operations = new ArrayList<>();

        // When
        List<Tax> result = handler.calculateTaxes(operations);

        // Then
        assertTrue(result.isEmpty());
        verifyNoInteractions(buyOperationHandler, sellOperationHandler);
    }

    @Test
    void shouldMaintainContextBetweenOperations() {
        // Given
        Operation buyOp1 = new Operation();
        buyOp1.setOperationType(OperationType.BUY);
        buyOp1.setQuantity(100);
        buyOp1.setUnitCost(10.0);

        Operation sellOp = new Operation();
        sellOp.setOperationType(OperationType.SELL);
        sellOp.setQuantity(50);
        sellOp.setUnitCost(15.0);

        Operation buyOp2 = new Operation();
        buyOp2.setOperationType(OperationType.BUY);
        buyOp2.setQuantity(50);
        buyOp2.setUnitCost(12.0);

        List<Operation> operations = Arrays.asList(buyOp1, sellOp, buyOp2);

        // When
        List<Tax> result = handler.calculateTaxes(operations);

        // Then
        verify(buyOperationHandler, times(2)).handle(any(), any());
        verify(sellOperationHandler).handle(any(), any());
        assertEquals(3, result.size());
    }

    @Test
    void shouldHandleDefaultConstructor() {
        // Given
        handler = new CapitalGainsTaxCalculatorHandlerImpl();
        Operation buyOp = new Operation();
        buyOp.setOperationType(OperationType.BUY);
        buyOp.setQuantity(100);
        buyOp.setUnitCost(10.0);

        List<Operation> operations = List.of(buyOp);

        // When
        List<Tax> result = handler.calculateTaxes(operations);

        // Then
        assertEquals(1, result.size());
    }

    @Test
    void shouldHandleComplexOperationSequence() {
        // Given
        List<Operation> operations = Arrays.asList(
                createOperation(OperationType.BUY, 100, 10.0),
                createOperation(OperationType.SELL, 50, 15.0),
                createOperation(OperationType.BUY, 75, 12.0),
                createOperation(OperationType.SELL, 100, 20.0)
        );

        // When
        List<Tax> result = handler.calculateTaxes(operations);

        // Then
        verify(buyOperationHandler, times(2)).handle(any(), any());
        verify(sellOperationHandler, times(2)).handle(any(), any());
        assertEquals(4, result.size());
    }

    @Test
    void shouldHandleOperationsWithDecimalValues() {
        // Given
        List<Operation> operations = Arrays.asList(
                createOperation(OperationType.BUY, 100, 10.55),
                createOperation(OperationType.SELL, 50, 15.75)
        );

        // When
        List<Tax> result = handler.calculateTaxes(operations);

        // Then
        verify(buyOperationHandler).handle(any(), any());
        verify(sellOperationHandler).handle(any(), any());
        assertEquals(2, result.size());
    }

    private Operation createOperation(OperationType type, int quantity, double unitCost) {
        Operation operation = new Operation();
        operation.setOperationType(type.name());
        operation.setQuantity(quantity);
        operation.setUnitCost(unitCost);
        return operation;
    }
}

