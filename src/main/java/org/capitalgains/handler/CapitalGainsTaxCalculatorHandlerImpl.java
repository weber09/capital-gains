package org.capitalgains.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.capitalgains.model.CapitalGainsContext;
import org.capitalgains.model.Operation;
import org.capitalgains.model.Tax;
import org.capitalgains.service.MediumPriceCalculator;
import org.capitalgains.service.MediumPriceCalculatorImpl;
import org.capitalgains.service.TaxCalculator;
import org.capitalgains.service.TaxCalculatorImpl;

import java.util.ArrayList;
import java.util.List;

public class CapitalGainsTaxCalculatorHandlerImpl implements CapitalGainsTaxCalculatorHandler {

    private final MediumPriceCalculator mediumPriceCalculator;
    private final TaxCalculator taxCalculator;
    private final ObjectMapper mapper;
    private final BuyOperationHandler buyOperationHandler;
    private final SellOperationHandler sellOperationHandler;

    public CapitalGainsTaxCalculatorHandlerImpl() {
        this.mediumPriceCalculator = new MediumPriceCalculatorImpl();
        this.taxCalculator = new TaxCalculatorImpl();
        this.mapper = new ObjectMapper();
        this.buyOperationHandler = new BuyOperationHandlerImpl();
        this.sellOperationHandler = new SellOperationHandlerImpl();
    }

    public CapitalGainsTaxCalculatorHandlerImpl(MediumPriceCalculator mediumPriceCalculator, TaxCalculator taxCalculator, BuyOperationHandler buyOperationHandler, SellOperationHandler sellOperationHandler) {
        this.mediumPriceCalculator = mediumPriceCalculator;
        this.taxCalculator = taxCalculator;
        this.mapper = new ObjectMapper();
        this.buyOperationHandler = buyOperationHandler;
        this.sellOperationHandler = sellOperationHandler;
    }

    @Override
    public List<Tax> calculateTaxes(List<Operation> operations) {
        CapitalGainsContext context = new CapitalGainsContext();
        List<Tax> taxes = new ArrayList<>();

        // Process each operation
        for (Operation operation : operations) {
            // Handle the operation based on its type
            switch (operation.getOperationType()) {
                case BUY:
                    this.handleBuyOperation(operation, context, taxes);
                    break;
                case SELL:
                    this.handleSellOperation(operation, context, taxes);
                    break;
                default:
                    // Handle unknown operation type
                    break;
            }
        }

        return taxes;
    }

    private void handleBuyOperation(Operation operation, CapitalGainsContext context, List<Tax> taxes) {
        Tax buyTax = this.buyOperationHandler.handle(operation, context);
        taxes.add(buyTax);
    }

    private void handleSellOperation(Operation operation, CapitalGainsContext context, List<Tax> taxes) {
        Tax sellTax = this.sellOperationHandler.handle(operation, context);
        taxes.add(sellTax);
    }
}
