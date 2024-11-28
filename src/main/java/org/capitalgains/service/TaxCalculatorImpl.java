package org.capitalgains.service;

public class TaxCalculatorImpl implements TaxCalculator {

    private final double minimumCost;

    private final double taxRate;

    public TaxCalculatorImpl() {
        this.taxRate = 0.2;
        this.minimumCost = 20000;
    }

    public TaxCalculatorImpl(double taxRate, double minimumCost) {
        this.taxRate = taxRate;
        this.minimumCost = minimumCost;
    }

    @Override
    public double calculateTax(double price, double mediumPrice, double lossValue, int quantity) {
        double totalCost = price * quantity;

        if (operationCostBellowThreshold(totalCost)
                || isLoss(price, mediumPrice)) {
            return 0;
        }

        double profit = (price - mediumPrice) * quantity;
        double taxBasis = profit - lossValue;
        return taxBasis > 0 ? taxBasis * taxRate : 0;
    }

    private boolean operationCostBellowThreshold(double totalCost) {
        return totalCost < this.minimumCost;
    }

    private boolean isLoss(double price, double mediumPrice) {
        return price < mediumPrice;
    }
}
