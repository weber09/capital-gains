package org.capitalgains.handler;

import org.capitalgains.model.CapitalGainsContext;
import org.capitalgains.model.Operation;
import org.capitalgains.model.Tax;
import org.capitalgains.service.MediumPriceCalculator;
import org.capitalgains.service.MediumPriceCalculatorImpl;

public class BuyOperationHandlerImpl implements BuyOperationHandler{
    private final MediumPriceCalculator mediumPriceCalculator;

    public BuyOperationHandlerImpl() {
        mediumPriceCalculator = new MediumPriceCalculatorImpl();
    }

    public BuyOperationHandlerImpl(MediumPriceCalculator mediumPriceCalculator) {
        this.mediumPriceCalculator = mediumPriceCalculator;
    }

    @Override
    public Tax handle(Operation operation, CapitalGainsContext context) {
        double mediumPrice = mediumPriceCalculator.calculate(
                operation.getUnitCost(),
                context.getMediumPriceValue(),
                context.getStocksQuantity(),
                operation.getQuantity()
        );
        context.addStocksQuantity(operation.getQuantity());
        context.setMediumPriceValue(mediumPrice);
        return new Tax(0);
    }
}
