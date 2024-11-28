package org.capitalgains.handler;

import org.capitalgains.model.CapitalGainsContext;
import org.capitalgains.model.Operation;
import org.capitalgains.model.Tax;
import org.capitalgains.service.TaxCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SellOperationHandlerImplTest {

    private SellOperationHandlerImpl sellOperationHandler;

    @Mock
    private TaxCalculator taxCalculator;

    private CapitalGainsContext context;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sellOperationHandler = new SellOperationHandlerImpl(taxCalculator);
        context = new CapitalGainsContext();
    }

    @Test
    void shouldHandleFirstSellOperation() {
        // Given
        context.setStocksQuantity(100);
        context.setMediumPriceValue(10.0);

        Operation operation = new Operation();
        operation.setQuantity(50);
        operation.setUnitCost(15.0);

        when(taxCalculator.calculateTax(15.0, 10.0, 0.0, 50)).thenReturn(50.0);

        // When
        Tax sellTax = sellOperationHandler.handle(operation, context);

        // Then
        assertEquals(50, context.getStocksQuantity());
        assertEquals(10.0, context.getMediumPriceValue());
        assertNotNull(sellTax);
        assertEquals(50.0, sellTax.getTax());

        verify(taxCalculator).calculateTax(15.0, 10.0, 0.0, 50);
    }

    @Test
    void shouldHandleSellOperationWithProfit() {
        // Given
        context.setStocksQuantity(100);
        context.setMediumPriceValue(10.0);

        Operation operation = new Operation();
        operation.setQuantity(50);
        operation.setUnitCost(20.0);

        when(taxCalculator.calculateTax(20.0, 10.0, 0.0, 50)).thenReturn(100.0);

        // When
        Tax sellTax = sellOperationHandler.handle(operation, context);

        // Then
        assertEquals(50, context.getStocksQuantity());
        assertEquals(10.0, context.getMediumPriceValue());
        assertEquals(0.0, context.getLossValue());
        assertNotNull(sellTax);
        assertEquals(100.0, sellTax.getTax());
    }

    @Test
    void shouldHandleSellOperationWithLoss() {
        // Given
        context.setStocksQuantity(100);
        context.setMediumPriceValue(20.0);

        Operation operation = new Operation();
        operation.setQuantity(50);
        operation.setUnitCost(15.0);

        when(taxCalculator.calculateTax(15.0, 20.0, 0.0, 50)).thenReturn(0.0);

        // When
        Tax sellTax = sellOperationHandler.handle(operation, context);

        // Then
        assertEquals(50, context.getStocksQuantity());
        assertEquals(20.0, context.getMediumPriceValue());
        assertEquals(250.0, context.getLossValue()); // (20-15) * 50 = 250
        assertNotNull(sellTax);
        assertEquals(0.0, sellTax.getTax());
    }

    @Test
    void shouldHandleSellOperationWithExistingLoss() {
        // Given
        context.setStocksQuantity(100);
        context.setMediumPriceValue(10.0);
        context.setLossValue(200.0);

        Operation operation = new Operation();
        operation.setQuantity(50);
        operation.setUnitCost(15.0);

        when(taxCalculator.calculateTax(15.0, 10.0, 200.0, 50)).thenReturn(0.0);

        // When
        Tax sellTax = sellOperationHandler.handle(operation, context);

        // Then
        assertEquals(50, context.getStocksQuantity());
        assertEquals(10.0, context.getMediumPriceValue());
        assertEquals(0.0, context.getLossValue()); // Previous loss should be consumed
        assertNotNull(sellTax);
        assertEquals(0.0, sellTax.getTax());
    }

    @Test
    void shouldHandleSellAllStocks() {
        // Given
        context.setStocksQuantity(100);
        context.setMediumPriceValue(10.0);

        Operation operation = new Operation();
        operation.setQuantity(100);
        operation.setUnitCost(15.0);

        when(taxCalculator.calculateTax(15.0, 10.0, 0.0, 100)).thenReturn(500.0);

        // When
        Tax sellTax = sellOperationHandler.handle(operation, context);

        // Then
        assertEquals(0, context.getStocksQuantity());
        assertEquals(10.0, context.getMediumPriceValue());
        assertNotNull(sellTax);
        assertEquals(500.0, sellTax.getTax());
    }

    @Test
    void shouldHandleMultipleSellOperations() {
        // Given
        context.setStocksQuantity(100);
        context.setMediumPriceValue(10.0);

        Operation operation1 = new Operation();
        operation1.setQuantity(30);
        operation1.setUnitCost(15.0);

        Operation operation2 = new Operation();
        operation2.setQuantity(30);
        operation2.setUnitCost(20.0);

        when(taxCalculator.calculateTax(15.0, 10.0, 0.0, 30)).thenReturn(150.0);
        when(taxCalculator.calculateTax(20.0, 10.0, 0.0, 30)).thenReturn(300.0);

        // When
        List<Tax> taxes = new ArrayList<>();
        taxes.add(sellOperationHandler.handle(operation1, context));
        taxes.add(sellOperationHandler.handle(operation2, context));

        // Then
        assertEquals(40, context.getStocksQuantity());
        assertEquals(10.0, context.getMediumPriceValue());
        assertEquals(2, taxes.size());
        assertNotNull(taxes.get(0));
        assertEquals(150.0, taxes.get(0).getTax());
        assertNotNull(taxes.get(1));
        assertEquals(300.0, taxes.get(1).getTax());
    }

    @Test
    void shouldHandleDefaultConstructor() {
        // Given
        sellOperationHandler = new SellOperationHandlerImpl();
        context.setStocksQuantity(100);
        context.setMediumPriceValue(10.0);

        Operation operation = new Operation();
        operation.setQuantity(50);
        operation.setUnitCost(15.0);

        // When
        Tax sellTax = sellOperationHandler.handle(operation, context);

        // Then
        assertEquals(50, context.getStocksQuantity());
        assertEquals(10.0, context.getMediumPriceValue());
        assertNotNull(sellTax);
    }

    @Test
    void shouldHandleSellOperationWithDecimalValues() {
        // Given
        context.setStocksQuantity(100);
        context.setMediumPriceValue(10.55);

        Operation operation = new Operation();
        operation.setQuantity(50);
        operation.setUnitCost(15.75);

        when(taxCalculator.calculateTax(15.75, 10.55, 0.0, 50)).thenReturn(260.0);

        // When
        Tax sellTax = sellOperationHandler.handle(operation, context);

        // Then
        assertEquals(50, context.getStocksQuantity());
        assertEquals(10.55, context.getMediumPriceValue());
        assertNotNull(sellTax);
        assertEquals(260.0, sellTax.getTax());
    }

    @Test
    void shouldHandleSellOperationWithPartialLossConsumption() {
        // Given
        context.setStocksQuantity(100);
        context.setMediumPriceValue(10.0);
        context.setLossValue(300.0);

        Operation operation = new Operation();
        operation.setQuantity(50);
        operation.setUnitCost(20.0);

        when(taxCalculator.calculateTax(20.0, 10.0, 300.0, 50)).thenReturn(100.0);

        // When
        Tax sellTax = sellOperationHandler.handle(operation, context);

        // Then
        assertEquals(50, context.getStocksQuantity());
        assertEquals(10.0, context.getMediumPriceValue());
        assertEquals(0.0, context.getLossValue());
        assertNotNull(sellTax);
        assertEquals(100.0, sellTax.getTax());
    }
}

