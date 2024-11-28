package org.capitalgains.handler;

import org.capitalgains.model.CapitalGainsContext;
import org.capitalgains.model.Operation;
import org.capitalgains.model.Tax;
import org.capitalgains.service.MediumPriceCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BuyOperationHandlerImplTest {

    private BuyOperationHandlerImpl buyOperationHandler;

    @Mock
    private MediumPriceCalculator mediumPriceCalculator;

    private CapitalGainsContext context;
    private List<Tax> taxes;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        buyOperationHandler = new BuyOperationHandlerImpl(mediumPriceCalculator);
        context = new CapitalGainsContext();
        taxes = new ArrayList<>();
    }

    @Test
    void shouldHandleFirstBuyOperation() {
        // Given
        Operation operation = new Operation();
        operation.setQuantity(100);
        operation.setUnitCost(10.0);
        when(mediumPriceCalculator.calculate(10.0, 0.0, 0, 100)).thenReturn(10.0);

        // When
        Tax buyTax = buyOperationHandler.handle(operation, context);

        // Then
        assertEquals(100, context.getStocksQuantity());
        assertEquals(10.0, context.getMediumPriceValue());
        assertNotNull(buyTax);
        assertEquals(0.0, buyTax.getTax());

        verify(mediumPriceCalculator).calculate(10.0, 0.0, 0, 100);
    }

    @Test
    void shouldHandleSubsequentBuyOperation() {
        // Given
        context.setStocksQuantity(100);
        context.setMediumPriceValue(10.0);

        Operation operation = new Operation();
        operation.setQuantity(50);
        operation.setUnitCost(15.0);

        when(mediumPriceCalculator.calculate(15.0, 10.0, 100, 50)).thenReturn(11.67);

        // When
        Tax buyTax = buyOperationHandler.handle(operation, context);

        // Then
        assertEquals(150, context.getStocksQuantity());
        assertEquals(11.67, context.getMediumPriceValue(), 0.01);
        assertNotNull(buyTax);
        assertEquals(0.0, buyTax.getTax());

        verify(mediumPriceCalculator).calculate(15.0, 10.0, 100, 50);
    }

    @Test
    void shouldHandleZeroQuantityBuyOperation() {
        // Given
        Operation operation = new Operation();
        operation.setQuantity(0);
        operation.setUnitCost(10.0);

        when(mediumPriceCalculator.calculate(anyDouble(), anyDouble(), anyInt(), anyInt()))
                .thenReturn(0.0);

        // When
        Tax buyTax = buyOperationHandler.handle(operation, context);

        // Then
        assertEquals(0, context.getStocksQuantity());
        assertEquals(0.0, context.getMediumPriceValue());
        assertNotNull(buyTax);
        assertEquals(0.0, buyTax.getTax());
    }

    @Test
    void shouldHandleLargeQuantityBuyOperation() {
        // Given
        Operation operation = new Operation();
        operation.setQuantity(10000);
        operation.setUnitCost(10.0);

        when(mediumPriceCalculator.calculate(10.0, 0.0, 0, 10000)).thenReturn(10.0);

        // When
        Tax buyTax = buyOperationHandler.handle(operation, context);

        // Then
        assertEquals(10000, context.getStocksQuantity());
        assertEquals(10.0, context.getMediumPriceValue());
        assertNotNull(buyTax);
        assertEquals(0.0, buyTax.getTax());

        verify(mediumPriceCalculator).calculate(10.0, 0.0, 0, 10000);
    }

    @Test
    void shouldHandleMultipleBuyOperations() {
        // Given
        context.setStocksQuantity(100);
        context.setMediumPriceValue(10.0);

        Operation operation1 = new Operation();
        operation1.setQuantity(50);
        operation1.setUnitCost(15.0);

        Operation operation2 = new Operation();
        operation2.setQuantity(50);
        operation2.setUnitCost(20.0);

        when(mediumPriceCalculator.calculate(15.0, 10.0, 100, 50)).thenReturn(11.67);
        when(mediumPriceCalculator.calculate(20.0, 11.67, 150, 50)).thenReturn(13.33);

        // When
        taxes.add(buyOperationHandler.handle(operation1, context));
        taxes.add(buyOperationHandler.handle(operation2, context));

        // Then
        assertEquals(200, context.getStocksQuantity());
        assertEquals(13.33, context.getMediumPriceValue(), 0.01);
        assertEquals(2, taxes.size());
        assertNotNull(taxes.get(0));
        assertEquals(0.0, taxes.get(0).getTax());
        assertNotNull(taxes.get(1));
        assertEquals(0.0, taxes.get(1).getTax());
    }

    @Test
    void shouldHandleDefaultConstructor() {
        // Given
        buyOperationHandler = new BuyOperationHandlerImpl();
        Operation operation = new Operation();
        operation.setQuantity(100);
        operation.setUnitCost(10.0);

        // When
        Tax buyTax = buyOperationHandler.handle(operation, context);

        // Then
        assertEquals(100, context.getStocksQuantity());
        assertEquals(10.0, context.getMediumPriceValue());
        assertNotNull(buyTax);
        assertEquals(0.0, buyTax.getTax());
    }

    @Test
    void shouldHandleBuyOperationWithDecimalValues() {
        // Given
        Operation operation = new Operation();
        operation.setQuantity(100);
        operation.setUnitCost(10.55);

        when(mediumPriceCalculator.calculate(10.55, 0.0, 0, 100)).thenReturn(10.55);

        // When
        Tax buyTax = buyOperationHandler.handle(operation, context);

        // Then
        assertEquals(100, context.getStocksQuantity());
        assertEquals(10.55, context.getMediumPriceValue());
        assertNotNull(buyTax);
        assertEquals(0.0, buyTax.getTax());

        verify(mediumPriceCalculator).calculate(10.55, 0.0, 0, 100);
    }
}
