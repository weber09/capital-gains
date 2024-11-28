package org.capitalgains.handler;

import org.capitalgains.model.CapitalGainsContext;
import org.capitalgains.model.Operation;
import org.capitalgains.model.Tax;
import org.capitalgains.service.TaxCalculator;
import org.capitalgains.service.TaxCalculatorImpl;

public class SellOperationHandlerImpl implements SellOperationHandler{

    private final TaxCalculator taxCalculator;

    public SellOperationHandlerImpl() {
        this.taxCalculator = new TaxCalculatorImpl();
    }

    public SellOperationHandlerImpl(TaxCalculator taxCalculator) {
        this.taxCalculator = taxCalculator;
    }

    @Override
    public Tax handle(Operation operation, CapitalGainsContext context) {
        double taxAmount = taxCalculator.calculateTax(
                operation.getUnitCost(),
                context.getMediumPriceValue(),
                context.getLossValue(),
                operation.getQuantity()
        );

        context.removeStocksQuantity(operation.getQuantity());
        updateContextLossValue(operation, context);
        return new Tax(taxAmount);
    }

    private void updateContextLossValue(Operation operation, CapitalGainsContext context) {
        double lossValue = getLossValue(operation, context);

        if (lossValue > 0) {
            context.addLossValue(lossValue);
        } else {
            double profit = (operation.getUnitCost() - context.getMediumPriceValue()) * operation.getQuantity();
            context.decreaseLossValue(profit);
        }
    }

    private double getLossValue(Operation operation, CapitalGainsContext context) {
        double lossValue = 0;
        if (operation.getUnitCost() < context.getMediumPriceValue()) {
            lossValue = (context.getMediumPriceValue() - operation.getUnitCost()) * operation.getQuantity();
        }
        return lossValue;
    }
}
